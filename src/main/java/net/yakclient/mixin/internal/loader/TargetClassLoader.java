package net.yakclient.mixin.internal.loader;

import net.yakclient.mixin.internal.bytecode.ByteCodeUtils;

import java.io.IOException;


public class TargetClassLoader extends ProxyClassLoader {
    private final PackageTarget target;
//    private final ProxyClassLoader lazyParent;

    public TargetClassLoader(ClassLoader parent, PackageTarget target) {
        super(parent);
//        this.lazyParent = parent;
        this.target = target;
    }

    @Override
    public Class<?> defineClass(String name, byte[] b) {
        return super.defineClass(name, b);
    }

    @Override
    protected final Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        final ClassTarget target = ClassTarget.create(name);
        if (!this.target.isTargetOf(target)) return super.loadClass(name, resolve);
        try {
            Class<?> c = this.findLoadedClass(name);

            if (c == null) {
                byte[] b = ByteCodeUtils.loadClassBytes(name);
                c = defineClass(name, b, 0, b.length);
            }

            if (resolve) this.resolveClass(c);

            return c;
        } catch (IOException e) {
            throw new ClassNotFoundException(e.getMessage());
        }
    }



    public PackageTarget getTarget() {
        return target;
    }
}
