package net.yakclient.mixin.base.internal.loader

open class ProxyClassLoader(parent: ClassLoader) : ClassLoader(parent) {
//    protected val defaultDomain = ProtectionDomain(
//        CodeSource(null, null as Array<Certificate?>?),
//        null, this, null
//    )

    open fun defineClass(name: String, b: ByteArray): Class<*> {
        return super.defineClass(name, b, 0, b.size)
    }

    //
    //
    fun isDefined(cls: String?): Boolean {
        return super.findLoadedClass(cls) != null
    } //    @Override

    //    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
    //        if (this.isDefined(CONTEXT_POOL_MANAGER) && ContextPoolManager.isTargeted(name))
    //            return ContextPoolManager.loadClass(name);
    //        return super.loadClass(name, resolve);
    //    }
    init {
        Thread.currentThread().contextClassLoader = this
    }
}