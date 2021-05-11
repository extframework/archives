package net.yakclient.mixin.base.internal.loader

import net.yakclient.mixin.base.target.Target

object ContextPoolManager {
    private val pool: ContextPool
    private val loader: ClassLoader
    init {
        pool = DynamicContextPool()
        loader = Thread.currentThread().contextClassLoader
    }

    private fun isTargeted(target: Target): Boolean {
        return this.pool.isTargeted(target)
    }

    /**
     * Targets a specific package or class as the reciprocal of reloading.
     * From now on this target will no longer be able to be loaded by a
     * `ProxyClassLoader` and everything will be delegated to the
     * context.
     *
     * In this case what is happening is that a target is merely being
     * held in place. Later this can be overridden
     *
     * @param target The target to apply.
     * @return The context associated with the new target. Note, This
     * should NOT BE STORED, to avoid all possible class loader leaks
     * this should not be saved.
     * @see Context
     *
     * @see ContextPool
     */
    @JvmStatic
    fun applyTarget(target: Target): Context {
        return this.pool.addTarget(target)
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
    @JvmStatic
    @Throws(ClassNotFoundException::class)
    fun loadClass(target: Target, name: String): Class<*> {
        if (!isTargeted(target)) this.loader.loadClass(name)
        return this.pool.loadClassOrNull(target, name)
            ?: throw ClassNotFoundException("Failed to find class: $name")
    }

    @JvmStatic
    fun defineClass(target: Target, name: String, bytes: ByteArray): Class<*> {
        return this.pool.defineClass(target, name, bytes)
    }

    @JvmStatic
    fun isDefined(target: Target, name: String): Boolean {
        return this.pool.isClassDefined(target, name)
    }

    /**
     * Creates a new TargetClassLoader targeted with the location given. For any
     * operation with MixinUtils the ProxyClassLoader must be the default but an
     * exception can be throw here if that is not the case.
     *
     *
     * A side note; The return of this method SHOULD NOT BE STORED! This could
     * very well cause massive leaks in class reloading through memory.
     *
     * @param target The String to target for.
     * @return The classloader produced.
     */
    @JvmStatic
    fun createLoader(target: Target): ProxyClassLoader {
        return target.createLoader(this.loader)
    }
}