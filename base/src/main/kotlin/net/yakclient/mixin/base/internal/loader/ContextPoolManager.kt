package net.yakclient.mixin.base.internal.loader

import net.yakclient.mixin.base.target.ModuleTarget
import net.yakclient.mixin.base.target.Target

object ContextPoolManager {
    val modulePool: ModuleContextPool = ModuleContextPool()
    val libPool: LibContextPool = LibContextPool()

    fun resolveModuleByClass(clsName: String): ModuleTarget? {
        for (target in modulePool.targets()) {
            if (target.hasClass(clsName)) return target
        }
        return null
    }

    fun resolveModuleByModule(module: String): ModuleTarget? {
        for (target in modulePool.targets()) {
            if (target.moduleName == module) return target
        }
        return null
    }


    /**
     * Loads a class or throws a ClassNotFound. If the given class isn't targeted
     * then it will default to a super implementation. Otherwise it will load from
     * the pool.
     *
     * @param name The name of the class to load.
     * @return The loaded class.
     * @throws ClassNotFoundException If this class isn't found.
     */
    @JvmStatic
    @Throws(ClassNotFoundException::class)
    fun <T : Target> loadClass(pool: ContextPool<T>, target: T, name: String): Class<*> {
        return pool.loadClassOrNull(target, name)
            ?: ClassLoader.getSystemClassLoader().loadClass(name)
    }
}