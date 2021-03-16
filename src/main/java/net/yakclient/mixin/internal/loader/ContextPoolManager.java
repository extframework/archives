package net.yakclient.mixin.internal.loader;

import org.jetbrains.annotations.NotNull;

public class ContextPoolManager {
    private static ContextPoolManager instance;
    private final ContextPool pool;
    private final ClassLoader loader;

    public ContextPoolManager() {
        this.pool = new DynamicContextPool();
        this.loader = Thread.currentThread().getContextClassLoader();
    }

    public static boolean isTargeted(String target) {
        return getInstance().pool.isTargeted(target);
    }

    public static boolean isTargeted(PackageTarget target) {
        return getInstance().pool.isTargeted(target);
    }

    /**
     * Targets a specific package or class as the reciprocal of reloading.
     * From now on this target will no longer be able to be loaded by a
     * {@code ProxyClassLoader} and everything will be delegated to the
     * context.
     * <p>
     * In this case what is happening is that a target is merely being
     * held in place. Later this can be overridden
     *
     * @param target The target to apply.
     * @return The context associated with the new target. Note, This
     * should NOT BE STORED, to avoid all possible class loader leaks
     * this should not be saved.
     * @see Context
     * @see ContextPool
     */
    public static Context applyTarget(PackageTarget target) {
        final ContextPoolManager instance = getInstance();
        return instance.pool.addTarget(target);
    }

    /**
     * Used to overload either a null context, override a existing one
     * or provide a totally new implementation of the target.
     *
     * @param target The target to be overridden/added.
     * @param loader The loader holding the implementation and target.
     * @return Context to the given target.
     * @see Context
     * @see ContextPool
     */
    public static PackageTarget applyTarget(PackageTarget target, @NotNull ClassLoader loader) {
        final ContextPoolManager instance = getInstance();
        instance.pool.addTarget(target, loader);
        return target;
    }

    /**
     * Loads a class or throws a ClassNotFound. If the given class isn't targeted
     * then it will default to a super implementation. Otherwise it will load from
     * the pool.
     *
     * @param name The name of the class to load.
     * @return The loaded class.
     * @throws ClassNotFoundException If this class isn't found.
     */
   public static Class<?> loadClass(String name) throws ClassNotFoundException {
        final ContextPoolManager manager = getInstance();

        if (!isTargeted(name)) manager.loader.loadClass(name);

        final Class<?> aClass = manager.pool.loadClassOrNull(name);
        if (aClass == null) throw new ClassNotFoundException("Failed to find class: " + name);

        return aClass;
    }

    /**
     * Creates a new TargetClassLoader targeted with the location given. For any
     * operation with MixinUtils the ProxyClassLoader must be the default but an
     * exception can be throw here if that is not the case.
     *
     * A side note; The return of this method SHOULD NOT BE STORED! This could
     * very well cause massive leaks in class reloading through memory.
     *
     * @param target The String to target for.
     * @return The classloader produced.
     */
    static TargetClassLoader createLoader(PackageTarget target) {
        final ContextPoolManager instance = getInstance();

        if (!(instance.loader instanceof ProxyClassLoader)) throw new IllegalStateException("Failed to provide the default classloader as " + ProxyClassLoader.class.getName());
        return new TargetClassLoader(instance.loader, instance.pool.getTarget(target));
    }

    public static ContextPoolManager getInstance() {
        if (instance == null) return (instance = new ContextPoolManager());
        return instance;
    }
}
