package net.yakclient.mixin.internal.loader;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Context {
    private final ClassLoader loader;

    private final PackageTarget target;

    private final ContextPool pool;

    public Context(ClassLoader loader, PackageTarget target, ContextPool pool) {
        this.loader = loader;
        this.target = target;
        this.pool = pool;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Context context = (Context) o;
        return Objects.equals(loader, context.loader) && Objects.equals(target, context.target) && Objects.equals(pool, context.pool);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loader, target, pool);
    }

    @Nullable
    public Class<?> findClass(String name) {
        try {
            if (this.getLoader() == null) throw new IllegalStateException("This context is currently unavailable.");

            return this.getLoader().loadClass(name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @Nullable
    public ClassLoader getLoader() {
        return loader;
    }

    public PackageTarget getTarget() {
        return target;
    }

    public ContextPool getPool() {
        return pool;
    }
}
