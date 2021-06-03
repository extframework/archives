package net.yakclient.mixin.base.target

import net.yakclient.mixin.base.internal.loader.ProxyClassLoader

interface Target {
    fun createLoader(parent: ClassLoader): ProxyClassLoader
    /**
    * Decides if the current path is equally or less
    * specific than then one given.
    *
    * @param target The path to compare against.
    * @return if the given path could be a child of the current target.
    */
    fun isTargetOf(target: Target): Boolean

    fun name(): String
}