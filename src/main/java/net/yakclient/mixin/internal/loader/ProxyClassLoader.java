package net.yakclient.mixin.internal.loader;

import java.io.IOException;

public class ProxyClassLoader extends ClassLoader {
    private static final String CONTEXT_POOL_MANAGER = "net.yakclient.mixin.internal.loader.ContextPoolManager";

    public ProxyClassLoader(ClassLoader parent) {
        super(parent);
        Thread.currentThread().setContextClassLoader(this);
    }

    public Class<?> defineClass(String name, byte[] b) {
        return this.defineClass(name, b, 0, b.length);
    }
//
//
    public boolean isDefined(String cls) {
        return super.findLoadedClass(cls) != null;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
//        if (super.findLoadedClass(CLASS_MANAGER) == null && !CLASS_MANAGER.equals(name)) this.loadClass(CLASS_MANAGER, true);
//        if (this.findLoadedClass(CLASS_MANAGER) != null || ClassManager.hasOverload(name))
//            return ClassManager.retrieve(name);
//        if (name.startsWith("java.")) return super.loadClass(name);
//
        if (this.isDefined(CONTEXT_POOL_MANAGER) && ContextPoolManager.isTargeted(name))
            return ContextPoolManager.loadClass(name);
        return super.loadClass(name, resolve);
    }

}
