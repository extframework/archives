package net.yakclient.mixin.internal.loader;

import java.util.Arrays;

public class PackageTarget {
    private final String[] path;

    public PackageTarget(String path) {
        this.path = fromPath(path);
    }

    public PackageTarget(String[] path) {
        this.path = path;
    }

    public static PackageTarget create(String path) {
        return new PackageTarget(fromPath(path));
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
        for (int i = 0; i < this.path.length; i++) isTarget = this.path[i].equals(target.path[i]);

        return isTarget;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PackageTarget that = (PackageTarget) o;
        return Arrays.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(path);
    }
}
