package net.yakclient.mixin.base.internal.loader;

import java.nio.ByteBuffer;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;

public class ProxyClassLoader extends ClassLoader {
    private static final String CONTEXT_POOL_MANAGER = "net.yakclient.mixin.internal.loader.ContextPoolManager";
    protected final ProtectionDomain defaultDomain =
            new ProtectionDomain(new CodeSource(null, (Certificate[]) null),
                    null, this, null);

    public ProxyClassLoader(ClassLoader parent) {
        super(parent);
        Thread.currentThread().setContextClassLoader(this);
    }

    public Class<?> defineClass(String name, byte[] b) {
        return this.defineClass(name, ByteBuffer.wrap(b), this.defaultDomain);
    }
//
//
    public boolean isDefined(String cls) {
        return super.findLoadedClass(cls) != null;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if (this.isDefined(CONTEXT_POOL_MANAGER) && ContextPoolManager.isTargeted(name))
            return ContextPoolManager.loadClass(name);
        return super.loadClass(name, resolve);
    }

}
