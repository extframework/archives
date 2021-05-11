package net.yakclient.mixin.base.internal.loader

import java.net.URL
import java.net.URLClassLoader

class ExternalJarLoader(
    url: URL,
    parent: ClassLoader,
) : URLClassLoader(arrayOf(url), parent), ProxyClassLoader {

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