package net.yakclient.mixin.registry.pool;

import net.yakclient.mixin.internal.bytecode.BytecodeMethodModifier;
import net.yakclient.mixin.internal.loader.ClassTarget;
import net.yakclient.mixin.internal.loader.ContextPoolManager;
import net.yakclient.mixin.internal.loader.PackageTarget;
import net.yakclient.mixin.internal.loader.ProxyClassLoader;
import net.yakclient.mixin.registry.FunctionalProxy;
import net.yakclient.mixin.registry.MixinMetaData;
import net.yakclient.mixin.registry.proxy.MixinProxyManager;

import java.io.IOException;
import java.util.UUID;
import java.util.stream.Collectors;

public class MixinRegistryPool extends RegistryPool<MixinMetaData> {
    //    private final Map<Location, byte[]> parents;
    /*
        The pool holds locations to where mixins will be injected. These will be and only be MethodLocations(No child).
        The PoolQueue
     */
    private final BytecodeMethodModifier methodModifier;

    public MixinRegistryPool() {
        this.methodModifier = new BytecodeMethodModifier();
//        this.parents = new HashMap<>();
    }

    @Override
    public Location pool(MixinMetaData type) {
        final MethodLocation key = MethodLocation.fromDataDest(type);
        if (!this.pool.containsKey(key))
            this.pool.put(key, new PoolQueue<>());

//        PointerManager.register(this.register(key));
        this.pool.get(key).add(type, (t)->{});
//        this.parents.putIfAbsent(key, type.getClassTo());

        return key;
    }

    /*
        Using the PointerManager#register() is a sub-optimal solution. This will(unfortunately) work. What is happening
        is that i am registering a null classloader to register a place keeper. Then overloading it once we have to
        complete our future.
     */
    public Location pool(MixinMetaData type, FunctionalProxy proxy) {
        final MethodLocation key = MethodLocation.fromDataDest(type);
        if (!this.pool.containsKey(key))
            this.pool.put(key, new PoolQueue<>());

//        final ProxiedPointer proxiedPointer =
        final UUID register = MixinProxyManager.register();
        this.pool.get(key).add(type, (t) -> MixinProxyManager.registerProxy(register, proxy), register);
//        this.parents.putIfAbsent(key, type.getClassTo());

        return key;
    }

    /*
        The registry combiner will take a QualifiedMethodLocation as the source and then a MethodLocation as the destination.
        It wont take a collection because in the end that doest make sense and makes it way more complicated
     */
    @Override
    public PackageTarget register(Location location) {
        try {
            final PoolQueue<MixinMetaData> pool = this.pool.get(location);

            if (!(location instanceof MethodLocation))
                throw new IllegalArgumentException("Provided location must be a MethodLocation!");
            final MethodLocation dest = (MethodLocation) location;

            final ClassTarget sysTarget = ClassTarget.create(((MethodLocation) location).getCls());
            final ProxyClassLoader loader = ContextPoolManager.createLoader(sysTarget);
            ContextPoolManager.applyTarget(sysTarget, loader);

            final BytecodeMethodModifier.Source[] sources = pool.queue.stream().map(node -> node instanceof PoolQueue.ProxiedPoolNode<?> ?
                    new BytecodeMethodModifier.ProxySource(QualifiedMethodLocation.fromDataOrigin(node.getValue()), ((PoolQueue.ProxiedPoolNode<MixinMetaData>) node).getProxy()) :
                    new BytecodeMethodModifier.Source(QualifiedMethodLocation.fromDataOrigin(node.getValue())))
                    .toArray(BytecodeMethodModifier.Source[]::new);

//            byte[] b = ;

//            final QualifiedMethodLocation[] sources = pool.queue.stream().map(node -> QualifiedMethodLocation.fromDataOrigin(node.getValue())).toArray(QualifiedMethodLocation[]::new);


////            this.pool.get(location).queue
//            if (location instanceof ProxiedMethodLocation) {
//                ProxiedMethodLocation proxy = (ProxiedMethodLocation) location;
//                b = this.methodModifier.combine(sources, dest, proxy.getProxy());
//            } else b = this.methodModifier.combine(sources, dest);

            loader.defineClass(dest.getCls().getName(), this.methodModifier.combine(sources, dest));

            for (PoolQueue.PoolNode<MixinMetaData> datum : pool.queue)
                datum.run(sysTarget);


//            for (MixinMetaData data : pools.queue.stream().sorted(new MixinSorter()).collect(Collectors.toList())) {
//                byte[] b;
//
//                final QualifiedMethodLocation source = QualifiedMethodLocation.fromDataOrigin(data);
//                final MethodLocation dest = MethodLocation.fromDataDest(data);
//
//                if (location instanceof QualifiedProxiedMethodLocation) {
//                    QualifiedProxiedMethodLocation proxy = (QualifiedProxiedMethodLocation) location;
//                    b = this.methodModifier.combine(source, dest, proxy.getProxy());
//                } else b = this.methodModifier.combine(source, dest);
//
//                loader.defineClass(dest.getCls().getName(), b);
//            }
            return sysTarget;
        } catch (IOException e) {
            throw new IllegalArgumentException("Given class has failed ASM reading. Ex: " + e.getMessage());
        }
    }
}
