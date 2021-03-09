package net.yakclient.mixin.internal.loader;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class TargetClassLoader extends ProxyClassLoader {
    private final PackageTarget target;
//    private final ProxyClassLoader lazyParent;

    public TargetClassLoader(ClassLoader parent, PackageTarget target) {
        super(parent);
//        this.lazyParent = parent;
        this.target = target;
    }

    @Override
    protected final Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        final ClassTarget target = ClassTarget.create(name);
        if (!this.target.isTargetOf(target)) return super.loadClass(name, resolve);
        try {
            Class<?> c = this.findLoadedClass(name);
//            getCL

            if (c == null) {
//                final InputStream in = this.getResourceAsStream(name.replace('.', '/') + ".class");

//                if (in == null) throw new ClassNotFoundException("Failed to find class " + name);

//                final int available = in.available();
//                final byte[] bytes = new byte[available];

//                final ByteBuffer buf = ByteBuffer.allocate();
//                while (in.available() > 0) buf.put((byte) in.read());
//                Channels.newChannel(in).read()

                byte[] b = loadClassBytes(name.replace('.', '/') + ".class");
                c = defineClass(name, b, 0, b.length);
//                resolveClass(c);
//                return c;

//                ByteBuffer.
//                try {
//                    in.read(bytes);
//                c = this.defineClass(name, buf, defaultDomain);
//                } catch (LinkageError e) {
//                    c = this.defineClass(name, bytes, 0, available);
//                }
            }
            if (resolve) this.resolveClass(c);

            return c;
        } catch (IOException e) {
            throw new ClassNotFoundException(e.getMessage());
        }
    }

    private byte[] loadClassBytes(String name) throws IOException, ClassNotFoundException {
        InputStream cIn = this.getResourceAsStream(name);
        if (cIn == null) throw new ClassNotFoundException("Failed to find class " + name);

        try (DataInputStream in = new DataInputStream(cIn)) {
            byte[] buf = new byte[cIn.available()];

            in.readFully(buf);
            return buf;
        }
    }
}
