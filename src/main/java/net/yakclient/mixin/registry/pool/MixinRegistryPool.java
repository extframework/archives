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
import java.util.*;

public class MixinRegistryPool extends RegistryPool<MixinMetaData> {
    /*
        The pool holds locations to where mixins will be injected. These will be and only be MethodLocations(No child).
        The PoolQueue
     */
    private final BytecodeMethodModifier methodModifier;

    public MixinRegistryPool() {
        this.methodModifier = new BytecodeMethodModifier();
    }

    @Override
    public Location pool(MixinMetaData type) {
        final ClassLocation key = ClassLocation.fromDataDest(type);
        if (!this.pool.containsKey(key))
            this.pool.put(key, new PoolQueue<>());

        this.pool.get(key).add(type, (t) -> {
        });

        return key;
    }

    /*
        Using the PointerManager#register() is a sub-optimal solution. This will(unfortunately) work. What is happening
        is that i am registering a null classloader to register a place keeper. Then overloading it once we have to
        complete our future.
     */
    public Location pool(MixinMetaData type, FunctionalProxy proxy) {
        final ClassLocation key = ClassLocation.fromDataDest(type);
        if (!this.pool.containsKey(key))
            this.pool.put(key, new PoolQueue<>());

        final UUID register = MixinProxyManager.register();
        MixinProxyManager.registerProxy(register, proxy);
        this.pool.get(key).add(type, (t) -> {}, register);

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

            if (!(location instanceof ClassLocation))
                throw new IllegalArgumentException("Provided location must be a ClassLocation!");

            final ClassLocation dest = (ClassLocation) location;

            final ClassTarget sysTarget = ClassTarget.create(((ClassLocation) location).getCls());
            final ProxyClassLoader loader = ContextPoolManager.createLoader(sysTarget);
            ContextPoolManager.applyTarget(sysTarget, loader);

            //Method destination, Destinations
            final Map<String, BytecodeMethodModifier.MixinDestination> perfectDestinations = new HashMap<>();

            for (PoolQueue.PoolNode<MixinMetaData> node : pool.queue) {
                final String method = node.getValue().getMethodTo();
                perfectDestinations.putIfAbsent(method, new BytecodeMethodModifier.MixinDestination(node.getValue().getClassTo().getName(), node.getValue().getMethodTo()));

                perfectDestinations.get(method).addSource(node instanceof PoolQueue.ProxiedPoolNode<?> ?
                        new BytecodeMethodModifier.MixinProxySource(QualifiedMethodLocation.fromDataOrigin(node.getValue()), ((PoolQueue.ProxiedPoolNode<MixinMetaData>) node).getProxy()) :
                        new BytecodeMethodModifier.MixinSource(QualifiedMethodLocation.fromDataOrigin(node.getValue())));
            }

            final Set<BytecodeMethodModifier.MixinDestination> destinations = new HashSet<>(perfectDestinations.values());
//            perfectDestinations.values().forEach(destinations::addAll);
            System.out.println(destinations);

            final byte[] b = this.methodModifier.combine(destinations.toArray(new BytecodeMethodModifier.MixinDestination[0]));
            loader.defineClass(dest.getCls().getName(), b);
            System.out.println(b.length);
            for (PoolQueue.PoolNode<MixinMetaData> datum : pool.queue)
                datum.run(sysTarget);

            return sysTarget;
        } catch (IOException e) {
            throw new IllegalArgumentException("Given class has failed ASM reading. Ex: " + e.getMessage());
        }
    }
}
