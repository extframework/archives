package net.yakclient.mixin.base.internal.loader

import net.yakclient.mixin.base.internal.bytecode.ByteCodeUtils.loadClassBytes
import net.yakclient.mixin.base.target.ClassTarget.Companion.of
import net.yakclient.mixin.base.target.PackageTarget
import java.io.IOException

class TargetClassLoader(
    parent: ClassLoader,
    private val target: PackageTarget
) : ClassLoader(parent), ProxyClassLoader {

    @Throws(ClassNotFoundException::class)
    override fun loadClass(name: String, resolve: Boolean): Class<*> {
        val target = of(name)
        return if (!this.target.isTargetOf(target)) super.loadClass(name, resolve) else try {
            var c = findLoadedClass(name)
            if (c == null) {
                val b = loadClassBytes(name)
                c = defineClass(name, b, 0, b!!.size)
            }
            if (resolve) resolveClass(c)
            c
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