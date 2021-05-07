package net.yakclient.mixin.base.internal.loader

import net.yakclient.mixin.base.internal.bytecode.ByteCodeUtils.loadClassBytes
import net.yakclient.mixin.base.internal.loader.ClassTarget.Companion.of
import java.io.IOException

class TargetClassLoader(
    parent: ClassLoader,
    private val target: PackageTarget
) : ProxyClassLoader(parent) {

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
}