package net.yakclient.mixin.base.registry

import net.yakclient.mixin.base.internal.loader.ContextPoolManager
import net.yakclient.mixin.base.target.JarTarget
import net.yakclient.mixin.base.target.ModuleTarget

interface ContextHandle {
    fun findClass(name: String): Class<*>
}

data class ModuleContextHandle(private val handle: ModuleTarget) : ContextHandle {
    override fun findClass(name: String): Class<*> {
        return ContextPoolManager.loadClass(ContextPoolManager.modulePool, handle, name)
    }
}

data class JarContextHandle(private val handle: JarTarget) : ContextHandle {
    override fun findClass(name: String): Class<*> {
        return ContextPoolManager.loadClass(ContextPoolManager.libPool, handle, name)
    }
}