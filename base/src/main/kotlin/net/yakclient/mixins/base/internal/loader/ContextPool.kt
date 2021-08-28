package net.yakclient.mixins.base.internal.loader

import net.yakclient.mixins.base.target.JarTarget
import net.yakclient.mixins.base.target.ModuleTarget
import net.yakclient.mixins.base.target.Target
import java.lang.module.FindException

const val JAVA_TARGET_PROPS = "yakclient.mixins.targets"
const val JAVA_PROPS_DELIMITER = ","

private fun <K, V> Map<K, V>.checkPresent(key: K, message: String): V {
    return checkNotNull(get(key)) { message }
}

abstract class ContextPool<T: Target> {
    protected val targets: MutableMap<T, Context> = HashMap()

    fun targets(): List<T> {
        return targets.keys.toList()
    }

    abstract fun addTarget(target: T)

    protected abstract fun defaultLoader(target: T): ProxyClassLoader

    fun isTargeted(target: T): Boolean {
        return targets.containsKey(target)
    }

    fun defineClass(target: T, name: String, bytes: ByteArray): Class<*> {
        return targets.checkPresent(target, "Failed to find appropriate context to define class in")
            .let {
                if (it.isDefined(name)) Context(defaultLoader(target)).also { context -> targets[target] = context }
                else it
            }.defineClass(name, bytes)
    }

    fun loadClassOrNull(target: T, name: String): Class<*>? {
        return targets.checkPresent(target, "Failed to find context from given target: ${target.name()}")
            .findClass(name)
    }
}

class ModuleContextPool : ContextPool<ModuleTarget>() {
    init {
        System.getProperty(JAVA_TARGET_PROPS).split(JAVA_PROPS_DELIMITER).map { name ->
            ModuleLayer.boot().findModule(name)
                .orElseThrow { FindException("Failed to fine module: $name") }
                .let { module ->
                    ModuleTarget(module!!).also {
                        targets[it] = Context(ModuleTargetLoader(it))
                    }
                }
        }
    }

    override fun addTarget(target: ModuleTarget) {
        throw IllegalAccessError("Targets must have been added via command arguments on startup.")
    }

    override fun defaultLoader(target: ModuleTarget): ProxyClassLoader {
        return ModuleTargetLoader(target)
    }
}

class LibContextPool: ContextPool<JarTarget>() {
    override fun addTarget(target: JarTarget) {
        targets[target] = Context(ExternalJarLoader(target.url))
    }

    override fun defaultLoader(target: JarTarget): ProxyClassLoader {
        return ExternalJarLoader(target.url)
    }
}
//
//abstract class `1ModuleContextPool` {
//    private val targets: MutableMap<Target, Context> =
//        mutableMapOf(*System.getProperty(JAVA_TARGET_PROPS).split(JAVA_PROPS_DELIMITER).map { name ->
//            ModuleLayer.boot().findModule(name)
//                .orElseThrow { FindException("Failed to fine module: $name") }
//                .let { module ->
//                    val target = ModuleTarget(module!!)
//                    Pair(target, Context(ModuleTargetLoader(target = target)))
//                }
//        }.toTypedArray())
//
////    private fun findModule(name: String): ResolvedModule? {
////        for (target in targets) {
////            if (LoaderUtils.packages(target.key).contains(LoaderUtils asClassPackage name)) return target.key
////        }
////        return null
////    }
////
////    private fun findContext(name: String): Context? {
////        for (target in targets) {
////            if (LoaderUtils.packages(target.key).contains(LoaderUtils asClassPackage name)) return target.value
////        }
////        return null
////    }
//
//    fun targets(): List<Target> {
//        return targets.keys.toList()
//    }
//
//    fun loadClassOrNull(target: Target, name: String): Class<*>? {
//        return targets.checkPresent(target, "Failed to find context from given target: ${target.name()}")
//            .findClass(name)
//    }
//
//    fun isTargeted(target: Target): Boolean {
//        return targets.containsKey(target)
//    }
//
//    fun isClassDefined(target: Target, name: String): Boolean {
//        return targets.checkPresent(target, "Failed to find context from given target: ${target.name()}")
//            .isDefined(name)
//    }
//
//    fun defineClass(target: Target, name: String, bytes: ByteArray): Class<*> {
//        return targets.checkPresent(target, "Failed to find appropriate context to define class in")
//            .let {
//                if (it.isDefined(name)) this.setContext(
//                    target,
//                    Context(target.createLoader())
//                ) else it
//            }.defineClass(name, bytes)
//    }
//
//    private fun setContext(target: Target, context: Context): Context {
//        targets[target] = context
//        return context
//    }
//}