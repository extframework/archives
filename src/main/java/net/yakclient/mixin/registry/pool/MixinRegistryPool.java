package net.yakclient.mixin.registry.pool;

import net.yakclient.mixin.internal.bytecode.ByteCodeUtils;
import net.yakclient.mixin.internal.bytecode.BytecodeMethodModifier;
import net.yakclient.mixin.internal.bytecode.MixinDestination;
import net.yakclient.mixin.internal.bytecode.MixinSource;
import net.yakclient.mixin.internal.loader.*;
import net.yakclient.mixin.registry.FunctionalProxy;
import net.yakclient.mixin.registry.MixinMetaData;
import net.yakclient.mixin.registry.proxy.MixinProxyManager;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
    public Location pool(MixinMetaData type) throws ClassNotFoundException {
        final ClassLocation key = ClassLocation.fromDataDest(type);
        if (!this.pool.containsKey(key))
            this.pool.put(key, new PoolQueue<>());

        this.pool.get(key).add(type, (t) -> { });

        return key;
    }

    /*
        Using the PointerManager#register() is a sub-optimal solution. This will(unfortunately) work. What is happening
        is that i am registering a null classloader to register a place keeper. Then overloading it once we have to
        complete our future.
     */
    public Location pool(MixinMetaData type, FunctionalProxy proxy) throws ClassNotFoundException {
        final ClassLocation key = ClassLocation.fromDataDest(type);
        if (!this.pool.containsKey(key))
            this.pool.put(key, new PoolQueue<>());

        final UUID register = MixinProxyManager.register();
        MixinProxyManager.registerProxy(register, proxy);
        this.pool.get(key).add(type, (t) -> {}, register);

        return key;
    }

    private byte[] loadClass(String name) throws ClassNotFoundException {
        try {
            return ByteCodeUtils.loadClassBytes(name);
        } catch (IOException e) {
            throw new ClassNotFoundException("Failed to load system resources for class: " + name);
        }
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

            //Method destination, Destinations
            final Map<String, MixinDestination.MixinDestBuilder> perfectDestinations = new HashMap<>();

            for (PoolQueue.PoolNode<MixinMetaData> node : pool.queue) {
                final String method = node.getValue().getMethodTo();
                //node.getValue().getClassTo()
                perfectDestinations.putIfAbsent(method, new MixinDestination.MixinDestBuilder(node.getValue().getMethodTo()));

                perfectDestinations.get(method).addSource(node instanceof PoolQueue.ProxiedPoolNode<?> ?
                        new MixinSource.MixinProxySource(QualifiedMethodLocation.fromDataOrigin(node.getValue()), ((PoolQueue.ProxiedPoolNode<MixinMetaData>) node).getProxy()) :
                        new MixinSource(QualifiedMethodLocation.fromDataOrigin(node.getValue())));
            }

            final byte[] b = this.methodModifier.combine(dest.getCls(), perfectDestinations.values().stream().map(MixinDestination.MixinDestBuilder::build).distinct().toArray(MixinDestination[]::new));
            ContextPoolManager.defineClass(dest.getCls(), b);

            for (PoolQueue.PoolNode<MixinMetaData> datum : pool.queue)
                datum.run(sysTarget);

            return sysTarget;
        } catch (IOException e) {
            throw new IllegalArgumentException("Given class has failed ASM reading. Ex: " + e.getMessage());
        }
    }
}
