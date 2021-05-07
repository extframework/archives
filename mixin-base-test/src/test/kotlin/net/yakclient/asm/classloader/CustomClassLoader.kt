package net.yakclient.asm.classloader

import java.io.IOException

class CustomClassLoader : ClassLoader() {
    fun defineClass(name: String?, b: ByteArray): Class<*> {
        return super.defineClass(name, b, 0, b.size)
    }

    @Throws(ClassNotFoundException::class)
    override fun loadClass(name: String, resolve: Boolean): Class<*> {
        return super.loadClass(name, resolve)
    }

    @Throws(IOException::class)
    fun loadSomeClass(b: ByteArray, name: String?): Class<*> {
        return this.defineClass(name, b)
    }
}