package net.yakclient.mixin.base.internal.loader

data class Context(private val loader: ClassLoader, private val target: PackageTarget, private val pool: ContextPool) {
    fun findClass(name: String): Class<*>? {
        return try {
            getLoader().loadClass(name)
        } catch (e: ClassNotFoundException) {
            null
        }
    }

    fun getLoader(): ProxyClassLoader {
        check(loader is TargetClassLoader) { "Class loader for target $target" }
        return loader
    }
}