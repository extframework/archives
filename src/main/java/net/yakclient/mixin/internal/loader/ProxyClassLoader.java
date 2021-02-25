package net.yakclient.mixin.internal.loader;

public class ProxyClassLoader extends ClassLoader {
    //([BLjava/lang/String;) Ljava/lang/Class
    public Class<?> defineClass(byte[] b, String name) {
        return super.defineClass(name, b, 0, b.length);
    }

    public boolean isDefined(String cls) {
        try {
            this.loadClass(cls);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if (ClassManager.hasOverload(name)) return ClassManager.retrieve(name);
        return super.loadClass(name, resolve);
    }
}
