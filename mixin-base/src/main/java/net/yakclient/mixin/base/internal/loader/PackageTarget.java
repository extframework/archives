package net.yakclient.mixin.base.internal.loader;

import java.util.Arrays;
import java.util.StringJoiner;

public class PackageTarget {
    protected final String[] path;

    public PackageTarget(String path) {
        this.path = fromPath(path);
    }

    public PackageTarget(String[] path) {
        this.path = path;
    }

    public static PackageTarget of(String path) {
        return new PackageTarget(fromPath(path));
    }

    public static PackageTarget of(Class<?> cls) {
        return new PackageTarget(fromPath(cls.getPackage().getName()));
    }


    protected static String[] fromPath(String path) {
        return path.split("\\.");
    }

    /**
     * Decides if the current path is equally or less
     * specific than then one given.
     *
     * @param target The path to compare against.
     * @return if the given path could be a child of the current target.
     */
    public boolean isTargetOf(PackageTarget target) {
        if (this.equals(target)) return true;

        boolean isTarget = false;
        for (int i = 0; i < this.path.length; i++) {
            isTarget = target.path.length > i && this.path[i].equals(target.path[i]);
        }

        return isTarget;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final var that = (PackageTarget) o;
        return Arrays.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(path);
    }

    @Override
    public String toString() {
        final var joiner = new StringJoiner(".");
        for (var s : this.path) joiner.add(s);
        return joiner.toString();
    }
}
