package net.yakclient.mixin.registry;

import java.util.ArrayList;
import java.util.List;

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

    public static class Configuration {
        private final List<String> packageBlockList;

        private final List<String> packageSafeList;

        private Configuration() {
            this.packageBlockList = DefaultConfiguration.BLOCK_LIST.instantiate();
            this.packageSafeList = DefaultConfiguration.SAFE_LIST.instantiate();
        }
    }

    @FunctionalInterface
    private interface FunctionalConfiguration<T> {
        T instantiate();
    }

    private static class DefaultConfiguration {
        public static final FunctionalConfiguration<List<String>> BLOCK_LIST = ArrayList::new;
        public static final FunctionalConfiguration<List<String>> SAFE_LIST = ArrayList::new;
    }
}
