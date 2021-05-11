package net.yakclient.mixin.base.registry

import net.yakclient.mixin.base.internal.loader.ContextPoolManager
import net.yakclient.mixin.base.target.Target

data class ContextHandle(private val handle: Target) {
    @Throws(ClassNotFoundException::class)

    fun findClass(name: String): Class<*> {
        return ContextPoolManager.loadClass(this.handle, name)
    }

    fun isDefined(name: String): Boolean {
        return ContextPoolManager.isDefined(this.handle, name)
    }
}