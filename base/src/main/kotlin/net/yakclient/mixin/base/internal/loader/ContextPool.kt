package net.yakclient.mixin.base.internal.loader

import net.yakclient.mixin.base.internal.loader.ContextPoolManager.createLoader
import net.yakclient.mixin.base.target.ClassTarget
import net.yakclient.mixin.base.target.PackageTarget
import net.yakclient.mixin.base.target.Target
import org.jetbrains.annotations.Contract

abstract class ContextPool {
    private val targets: MutableMap<Target, Context>
    private val loader: ClassLoader

    init {
        targets = HashMap()
        loader = ClassLoader.getSystemClassLoader()
    }

    private fun safelyEncapsulate(target: Target): Target {
        for (encapsulated in targets.keys) {
            if (encapsulated.isTargetOf(target)) return encapsulated
        }
        return target
    }

    fun addTarget(target: Target): Context {
        val realTarget = safelyEncapsulate(target)
        val loader: ProxyClassLoader =
            if (targets.containsKey(realTarget)) targets[realTarget]!!.loader else createLoader(realTarget)
        val context = createContext(loader, target)
        if (!targets.containsKey(realTarget)) targets[realTarget] = context
        return context
    }

    fun isTargeted(target: Target): Boolean {
        for (pT in targets.keys) {
            if (pT.isTargetOf(target)) return true
        }
        return false
    }

    fun loadClassOrNull(target: Target, name: String): Class<*>? {
        val context = checkNotNull(targets[target]) { "Failed to find context from given target: ${target.name()}" }
        return context.findClass(name)
    }

    fun isClassDefined(target: Target, name: String): Boolean {
        val context = checkNotNull(targets[target]) { "Failed to find context from given target: ${target.name()}" }
        return context.loader.isDefined(name)
    }

    fun defineClass(target: Target, name: String, bytes: ByteArray): Class<*> {
        val finalTarget = safelyEncapsulate(target)
        val oldContext = checkNotNull(targets[finalTarget]) { "Failed to find appropriate context to define class in" }

        val context = if (oldContext.loader.isDefined(name)) this.setContext(
            finalTarget,
            createContext(createLoader(finalTarget), finalTarget)
        ) else oldContext

        return context.loader.defineClass(name, bytes)
    }

    private fun setContext(target: Target, context: Context): Context {
        targets[target] = context
        return context
    }

    private fun createContext(loader: ProxyClassLoader, target: Target): Context {
        return Context(loader, target, this)
    }
}