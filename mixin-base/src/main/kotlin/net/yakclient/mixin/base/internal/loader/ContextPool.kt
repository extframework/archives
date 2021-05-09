package net.yakclient.mixin.base.internal.loader

import net.yakclient.mixin.base.internal.loader.ContextPoolManager.createLoader
import org.jetbrains.annotations.Contract

abstract class ContextPool {
    private val targets: MutableMap<PackageTarget, Context>
    private val loader: ClassLoader

    init {
        targets = HashMap()
        loader = ClassLoader.getSystemClassLoader()
    }

    private fun encapsulatedTargetExists(target: PackageTarget): Boolean {
        for (packageTarget in targets.keys) {
            if (packageTarget.isTargetOf(target)) return true
        }
        return false
    }

    private fun encapsulatedTarget(target: PackageTarget): PackageTarget {
        check(encapsulatedTargetExists(target)) { "Target MUST be encapsulated!" }
        for (packageTarget in targets.keys) {
            if (packageTarget.isTargetOf(target)) return packageTarget
        }
        throw IllegalStateException("Target MUST be encapsulated!")
    }

    fun addTarget(target: PackageTarget, loader: ClassLoader): Context? {
        val realTarget = getTarget(target)
        return targets.put(realTarget, createContext(loader, realTarget))
    }

    fun addTarget(target: PackageTarget): Context {
        val realTarget = getTarget(target)
        val loader: ClassLoader =
            if (targets.containsKey(realTarget)) targets[realTarget]!!.getLoader() else createLoader(realTarget)
        val context = createContext(loader, target)
        if (!targets.containsKey(realTarget)) targets[realTarget] = context
        return context
    }

    @Contract(pure = true)
    fun getTarget(target: PackageTarget): PackageTarget {
        return if (encapsulatedTargetExists(target)) encapsulatedTarget(target) else target
    }

    fun isTargeted(target: String): Boolean {
        val packageTarget = createTarget(target)
        return this.isTargeted(packageTarget)
    }

    fun isTargeted(target: PackageTarget?): Boolean {
        for (pT in targets.keys) {
            if (pT.isTargetOf(target!!)) return true
        }
        return false
    }

    fun loadClassOrNull(name: String): Class<*>? {
        val target = getTarget(createTarget(name))
        return targets[target]!!.findClass(name)
    }

    private fun createContext(loader: ClassLoader, target: PackageTarget): Context {
        return Context(loader, target, this)
    }

    private fun createTarget(path: String): PackageTarget {
        return try {
            if (loader.loadClass(path) != null) ClassTarget.of(path) else PackageTarget.of(path)
        } catch (e: ClassNotFoundException) {
            PackageTarget.of(path)
        }
    }

    fun defineClass(target: ClassTarget, bytes: ByteArray): Class<*> {
        val finalTarget = getTarget(target)
        val name = target.name()
        val oldContext = targets[finalTarget]
        require(oldContext != null) { "Failed to find appropriate context to define class in" }

        val context = if (oldContext.getLoader().isDefined(name)) this.setContext(
            finalTarget,
            createContext(createLoader(finalTarget), finalTarget)
        ) else oldContext

        return context.getLoader().defineClass(name, bytes)
    }

    private fun setContext(target: PackageTarget, context: Context): Context {
        targets[target] = context
        return context
    }
}