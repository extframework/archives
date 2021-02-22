package net.yakclient.asm.classloader;

import java.io.IOException;

public class CustomClassLoader extends ClassLoader {
    public Class<?> defineClass(String name, byte[] b) {
        return super.defineClass(name, b, 0, b.length);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        return super.loadClass(name, resolve);
    }

    public Class<?> loadSomeClass(byte[] b, String name) throws IOException {
        return this.defineClass(name, b);
    }
}
