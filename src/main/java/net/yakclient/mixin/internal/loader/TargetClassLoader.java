package net.yakclient.mixin.internal.loader;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TargetClassLoader extends ProxyClassLoader {
    private final PackageTarget target;
    private final Map<String, byte[]> overridden;
//    private final ProxyClassLoader lazyParent;

    public TargetClassLoader(ClassLoader parent, PackageTarget target) {
        super(parent);
//        this.lazyParent = parent;
        this.target = target;
        this.overridden = new HashMap<>();
    }

    @Override
    public Class<?> defineClass(String name, byte[] b) {
        try {
            if (loadClassBytes(name).length != b.length) this.overridden.put(name, b);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return super.defineClass(name, b);
    }

    @Override
    protected final Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        final ClassTarget target = ClassTarget.create(name);
        if (!this.target.isTargetOf(target)) return super.loadClass(name, resolve);
        try {
            Class<?> c = this.findLoadedClass(name);

            if (c == null) {
                byte[] b = loadClassBytes(name);
                c = defineClass(name, b, 0, b.length);
            }

            if (resolve) this.resolveClass(c);

            return c;
        } catch (IOException e) {
            throw new ClassNotFoundException(e.getMessage());
        }
    }

    public static byte[] loadClassBytes(String name) throws IOException, ClassNotFoundException {
        InputStream cIn = ClassLoader.getSystemResourceAsStream(name.replace('.', '/') + ".class");
        if (cIn == null) throw new ClassNotFoundException("Failed to find class " + name);

        try (DataInputStream in = new DataInputStream(cIn)) {
            byte[] buf = new byte[cIn.available()];

            in.readFully(buf);
            return buf;
        }
    }
}
