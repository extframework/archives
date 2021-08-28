package net.yakclient.mixins.base.internal.loader

data class Context(private val loader: ProxyClassLoader) {
    fun findClass(name: String): Class<*>? {
        return try {
            loader.loadClass(name)
        } catch (e: ClassNotFoundException) {
           null
        }
    }

    fun isDefined(name: String): Boolean {
        return loader.isDefined(name)
    }

    fun defineClass(name: String, b: ByteArray): Class<*> {
        return loader.defineClass(name, b)
    }

}