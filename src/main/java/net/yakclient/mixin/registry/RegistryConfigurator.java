package net.yakclient.mixin.registry;

import net.yakclient.mixin.internal.loader.ClassTarget;
import net.yakclient.mixin.internal.loader.PackageTarget;
import net.yakclient.mixin.registry.pool.SuppliedFuture;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        return MixinRegistry.create(this.configuration);
    }

    public static void safeTarget(Configuration config, PackageTarget target) {
        if (config.packageSafeList.size() > 0 && !contains(config.packageSafeList, target))
            throw new IllegalArgumentException("Class package is not on the whitelist!");
        if (contains(config.packageBlockList, target))
            throw new IllegalArgumentException("This package has been blocked from mixins!");
    }

    private static boolean contains(Set<PackageTarget> targets, PackageTarget target) {
        for (var packageTarget : targets) {
            if (target instanceof ClassTarget && packageTarget.equals(target)) return true;
            if (!(target instanceof ClassTarget) && packageTarget.isTargetOf(target)) return true;
        }
        return false;
    }


    /*
        Public actions
     */
    public RegistryConfigurator addSafePackage(String location) {
        this.configuration.packageSafeList.add(PackageTarget.create(location));
        return this;
    }

    public RegistryConfigurator addBlockedPackage(String location) {
        this.configuration.packageBlockList.add(PackageTarget.create(location));
        return this;
    }

    public RegistryConfigurator addTarget(PackageTarget target) {
        safeTarget(this.configuration, target);
        this.configuration.targets.add(target);
        return this;
    }

    public static class Configuration {
        final Set<PackageTarget> packageBlockList;

        final Set<PackageTarget> packageSafeList;

        final Set<PackageTarget> targets;

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
        public static final FunctionalConfiguration<Set<PackageTarget>> BLOCK_LIST = HashSet::new;
        public static final FunctionalConfiguration<Set<PackageTarget>> SAFE_LIST = HashSet::new;
        public static final FunctionalConfiguration<Set<PackageTarget>> TARGETS = HashSet::new;
    }
}
