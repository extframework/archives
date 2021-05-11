package net.yakclient.mixin.base.internal.loader

interface ProxyClassLoader {
//    protected val defaultDomain = ProtectionDomain(
//        CodeSource(null, null as Array<Certificate?>?),
//        null, this, null
//    )

    fun defineClass(name: String, b: ByteArray): Class<*>
//
//    {
//        return super.defineClass(name, b, 0, b.size)
//    }

    //
    //
    fun isDefined(cls: String): Boolean

    fun loadClass(name: String): Class<*>

//    {
//        return super.findLoadedClass(cls) != null
//    } //    @Override

    //    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
    //        if (this.isDefined(CONTEXT_POOL_MANAGER) && ContextPoolManager.isTargeted(name))
    //            return ContextPoolManager.loadClass(name);
    //        return super.loadClass(name, resolve);
    //    }

}