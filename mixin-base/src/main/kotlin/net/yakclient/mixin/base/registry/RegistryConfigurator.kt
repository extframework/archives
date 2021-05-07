package net.yakclient.mixin.base.registry

import net.yakclient.mixin.base.internal.loader.ClassTarget
import net.yakclient.mixin.base.internal.loader.PackageTarget

class RegistryConfigurator private constructor() {
    private val configuration: Configuration
    init {
        configuration = Configuration()
    }
    fun create(): MixinRegistry {
        return MixinRegistry.create(configuration)
    }

    /*
        Public actions
     */
    fun addSafePackage(location: String): RegistryConfigurator {
        configuration.packageSafeList.add(PackageTarget.of(location))
        return this
    }

    fun addBlockedPackage(location: String): RegistryConfigurator {
        configuration.packageBlockList.add(PackageTarget.of(location))
        return this
    }

    fun addTarget(target: String): RegistryConfigurator {
        val packageTarget = PackageTarget.of(target)
        safeTarget(configuration, packageTarget)
        configuration.targets.add(packageTarget)
        return this
    }

    class Configuration {
        val packageBlockList: MutableSet<PackageTarget>
        val packageSafeList: MutableSet<PackageTarget>
        val targets: MutableSet<PackageTarget>

        init {
            packageBlockList = HashSet()
            packageSafeList = HashSet()
            targets = HashSet()
        }
    }

    companion object {
        /*
        Utilities for the lifetime of this class
     */
        fun configure(): RegistryConfigurator {
            return RegistryConfigurator()
        }

        fun safeTarget(config: Configuration, target: PackageTarget) {
            require(
                !(config.packageSafeList.size > 0 && !contains(
                    config.packageSafeList,
                    target
                ))
            ) { "Class package is not on the whitelist!" }
            require(!contains(config.packageBlockList, target)) { "This package has been blocked from mixins!" }
        }

        private fun contains(targets: Set<PackageTarget>, target: PackageTarget): Boolean {
            for (packageTarget in targets) {
                if (target is ClassTarget && packageTarget.equals(target)) return true
                if (target !is ClassTarget && packageTarget.isTargetOf(target)) return true
            }
            return false
        }
    }


}