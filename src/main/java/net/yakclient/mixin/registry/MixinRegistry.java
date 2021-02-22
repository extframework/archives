package net.yakclient.mixin.registry;

import net.yakclient.mixin.registry.pool.ExternalLibRegistryPool;
import net.yakclient.mixin.registry.pool.MixinRegistryPool;
import net.yakclient.mixin.registry.pool.RegistryPool;

import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class MixinRegistry {
    private final RegistryConfigurator.Configuration configuration;
    private final RegistryPool<URL> libRegistry;
    private final RegistryPool<MixinMetaData> mixinRegistry;

    MixinRegistry(RegistryConfigurator.Configuration configuration) {
        this.configuration = configuration;
        this.libRegistry = new ExternalLibRegistryPool();
        this.mixinRegistry = new MixinRegistryPool();
    }

    public CompletableFuture<Pointer> registerMixin(Class<?> cls) {
        return this.mixinRegistry.pool(this.data(cls));
    }

    public CompletableFuture<Pointer> registerMixin(Class<?> cls, FunctionalProxy proxy) {

    }

    private MixinMetaData data(Class<?> cls) {
        return null;
    }

//    private CompletableFuture<Pointer> registerMixin(MixinMetaData)


//    public ClassLoader retrieveLoader(UUID)
}
