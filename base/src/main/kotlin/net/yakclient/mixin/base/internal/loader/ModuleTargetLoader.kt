package net.yakclient.mixin.base.internal.loader

import net.yakclient.mixin.base.internal.bytecode.ByteCodeUtils.loadClassBytes
import net.yakclient.mixin.base.target.ModuleTarget
import java.io.IOException


class ModuleTargetLoader(
    private val target: ModuleTarget,
    parent: ClassLoader = getSystemClassLoader()
) : ClassLoader(parent), ProxyClassLoader {

    @Throws(ClassNotFoundException::class)
    override fun loadClass(name: String, resolve: Boolean): Class<*> {
        return if (!target.hasClass(name)) super.loadClass(
            name,
            resolve
        ) else try {
            return (findLoadedClass(name) ?: loadClassBytes(name).let {
                defineClass(
                    name,
                    it,
                    0,
                    it!!.size
                )
            }).also { if (resolve) resolveClass(it) }
        } catch (e: IOException) {
            throw ClassNotFoundException(e.message)
        }
    }

    override fun defineClass(name: String, b: ByteArray): Class<*> {
        return this.defineClass(name, b, 0, b.size)
    }

    override fun isDefined(cls: String): Boolean {
        return this.findLoadedClass(cls) != null
    }

    override fun loadClass(name: String): Class<*> {
        return super.loadClass(name)
    }
}