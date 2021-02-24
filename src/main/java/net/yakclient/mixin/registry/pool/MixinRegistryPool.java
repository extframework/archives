package net.yakclient.mixin.registry.pool;

import net.yakclient.mixin.registry.FunctionalProxy;
import net.yakclient.mixin.registry.MixinMetaData;
import net.yakclient.mixin.registry.PointerManager;
import net.yakclient.mixin.registry.RegistryPointer;
import net.yakclient.mixin.registry.proxy.MixinProxyManager;
import net.yakclient.mixin.registry.proxy.ProxiedPointer;

public class MixinRegistryPool extends RegistryPool<MixinMetaData> {
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
    public ClassLoader register(Location location) {
        return null;
    }
}
