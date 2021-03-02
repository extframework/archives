package net.yakclient.mixin.registry;

import net.yakclient.mixin.internal.loader.PackageTarget;
import net.yakclient.mixin.registry.pool.SuppliedFuture;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class RegistryConfigurator {
    private final Configuration configuration;

    private RegistryConfigurator() {
        this.configuration = new Configuration();
    }

    /*
        Utilities for the lifetime of this class
     */

    public static RegistryConfigurator configure() {
        return new RegistryConfigurator();
    }

    public MixinRegistry create() {
        return new MixinRegistry(this.configuration);
    }

    public RegistryConfigurator addSafePackage(String location) {
        this.configuration.packageSafeList.add(location);
        return this;
    }

    public RegistryConfigurator addBlockedPackage(String location) {
        this.configuration.packageBlockList.add(location);
        return this;
    }

    public SuppliedFuture<Pointer> addTarget(PackageTarget target) {
        final SuppliedFuture<Pointer> e = new SuppliedFuture<>(()->
            new RegistryPointer(PointerManager.register(target))
        );
        this.configuration.targets.add(e);
        return e;
    }

    public static class Configuration {
        final List<String> packageBlockList;

        final List<String> packageSafeList;

        final Set<SuppliedFuture<Pointer>> targets;

        private Configuration() {
            this.packageBlockList = DefaultConfiguration.BLOCK_LIST.instantiate();
            this.packageSafeList = DefaultConfiguration.SAFE_LIST.instantiate();
            this.targets = DefaultConfiguration.TARGETS.instantiate();
        }


    }

    @FunctionalInterface
    private interface FunctionalConfiguration<T> {
        T instantiate();
    }

    private static class DefaultConfiguration {
        public static final FunctionalConfiguration<List<String>> BLOCK_LIST = ArrayList::new;
        public static final FunctionalConfiguration<List<String>> SAFE_LIST = ArrayList::new;
        public static final FunctionalConfiguration<Set<SuppliedFuture<Pointer>>> TARGETS = HashSet::new;
    }
}
