package net.yakclient.mixin.base.internal.loader

import net.yakclient.mixin.base.target.Target

data class Context(val loader: ProxyClassLoader, private val target: Target, private val pool: ContextPool) {
    fun findClass(name: String): Class<*>? {
        return try {
            loader.loadClass(name)
        } catch (e: ClassNotFoundException) {
           null
        }
    }
}