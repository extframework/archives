package net.yakclient.mixin.registry.pool;

import net.yakclient.mixin.internal.bytecode.BytecodeMethodModifier;
import net.yakclient.mixin.internal.loader.ClassTarget;
import net.yakclient.mixin.internal.loader.ContextPoolManager;
import net.yakclient.mixin.internal.loader.PackageTarget;
import net.yakclient.mixin.internal.loader.ProxyClassLoader;
import net.yakclient.mixin.registry.FunctionalProxy;
import net.yakclient.mixin.registry.MixinMetaData;
import net.yakclient.mixin.registry.PointerManager;
import net.yakclient.mixin.registry.RegistryPointer;
import net.yakclient.mixin.registry.proxy.MixinProxyManager;
import net.yakclient.mixin.registry.proxy.ProxiedPointer;

import java.io.IOException;
import java.util.Comparator;
import java.util.stream.Collectors;

public class MixinRegistryPool extends RegistryPool<MixinMetaData> {
    private final BytecodeMethodModifier methodModifier;

    public MixinRegistryPool() {
        this.methodModifier = new BytecodeMethodModifier();
    }


    @Override
    public Location pool(MixinMetaData type) {
        final QualifiedMethodLocation key = QualifiedMethodLocation.fromDataDest(type);
        if (!this.pool.containsKey(key))
            this.pool.put(key, new PoolQueue<>(new SuppliedFuture<>(() -> new RegistryPointer(PointerManager.register(this.register(key))))));

        this.pool.get(key).add(type);

        return key;
    }

    /*
        Using the PointerManager#register() is a sub-optimal solution. This will(unfortunately) work. What is happening
        is that i am registering a null classloader to register a place keeper. Then overloading it once we have to
        complete our future.
     */
    public Location pool(MixinMetaData type, FunctionalProxy proxy) {
        final ProxiedMethodLocation key = ProxiedMethodLocation.fromDataDest(type, PointerManager.register());
        if (!this.pool.containsKey(key))
            this.pool.put(key, new PoolQueue<>(new SuppliedFuture<>(() -> {
                final ProxiedPointer proxiedPointer = MixinProxyManager.registerProxy(key.getProxy(), proxy);
                PointerManager.overload(key.getProxy(), this.register(key));
                return proxiedPointer;
            })));

        this.pool.get(key).add(type);

        return key;
    }

    /*
        The registry combiner will take a QualifiedMethodLocation as the source and then a MethodLocation as the destination.
        It wont take a collection because in the end that doest make sense and makes it way more complicated
     */
    @Override
    public PackageTarget register(Location location) {
        try {
            final PoolQueue<MixinMetaData> pools = this.pool.get(location);
            if (!(location instanceof MethodLocation)) throw new IllegalArgumentException("Provided location must be a MethodLocation!");

            final ClassTarget sysTarget = ClassTarget.create(((MethodLocation) location).getCls());
            final ProxyClassLoader loader = ContextPoolManager.createLoader(sysTarget);
            ContextPoolManager.applyTarget(sysTarget, loader);

            for (MixinMetaData data : pools.queue.stream().sorted(new MixinSorter()).collect(Collectors.toList())) {
                byte[] b;

                final QualifiedMethodLocation source = QualifiedMethodLocation.fromDataOrigin(data);
                final MethodLocation dest = MethodLocation.fromDataDest(data);

                if (location instanceof ProxiedMethodLocation) {
                    ProxiedMethodLocation proxy = (ProxiedMethodLocation) location;
                    b = this.methodModifier.combine(source, dest, proxy.getProxy());
                } else b = this.methodModifier.combine(source, dest);

                loader.defineClass(dest.getCls().getName(), b);
            }
            return sysTarget;
        } catch (IOException e) {
            throw new IllegalArgumentException("Given class has failed ASM reading. E: " + e.getMessage());
        }
    }

    private static class MixinSorter implements Comparator<MixinMetaData> {
        @Override
        public int compare(MixinMetaData o1, MixinMetaData o2) {
            return o1.getPriority() - o2.getPriority();
        }
    }
}
