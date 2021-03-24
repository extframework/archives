package net.yakclient.mixin.internal.loader;

import net.yakclient.mixin.internal.bytecode.ByteCodeUtils;

import java.util.Objects;

public class ClassTarget extends PackageTarget {
    private final String cls;

    public ClassTarget(String[] path, String cls) {
        super(path);
        this.cls = cls;
    }

    public static ClassTarget create(String path) {
        try {
            if (ByteCodeUtils.classExists(path)) {
                final String[] fromPath = PackageTarget.fromPath(path);

                final int i = fromPath.length - 1;
                final String[] split = new String[i];

                System.arraycopy(fromPath, 0, split, 0, i);

                return new ClassTarget(split, fromPath[i]);
            } else throw new ClassNotFoundException("Failed to find specified class");
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Invalid class path");
        }
    }

    public static ClassTarget create(Class<?> path) {
        final String[] fromPath = PackageTarget.fromPath(path.getName());

        final int i = fromPath.length - 1;
        final String[] split = new String[i];

        System.arraycopy(fromPath, 0, split, 0, i);

        return new ClassTarget(split, fromPath[i]);
    }

    public PackageTarget toPackage() {
        return new PackageTarget(this.path);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ClassTarget that = (ClassTarget) o;
        return Objects.equals(cls, that.cls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cls);
    }

    @Override
    public String toString() {
        return super.toString() + "." + this.cls;
    }
}
