package net.yakclient.mixins.base.internal.loader

interface ProxyClassLoader {
    fun defineClass(name: String, b: ByteArray): Class<*>

    fun isDefined(cls: String): Boolean

    fun loadClass(name: String): Class<*>
}