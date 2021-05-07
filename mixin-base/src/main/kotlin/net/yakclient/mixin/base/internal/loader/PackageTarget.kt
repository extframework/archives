package net.yakclient.mixin.base.internal.loader

import java.util.*

open class PackageTarget(protected val path: List<String>) {
//    @JvmField
//    protected val path: Array<String>

//    constructor(path: String) {
//        this.path = fromPath(path)
//    }
//
//    constructor(path: Array<String>) {
//        this.path = path
//    }

    /**
     * Decides if the current path is equally or less
     * specific than then one given.
     *
     * @param target The path to compare against.
     * @return if the given path could be a child of the current target.
     */
    fun isTargetOf(target: PackageTarget): Boolean {
        if (this == target) return true
        var isTarget = false
        for (i in path.indices) {
            isTarget = target.path.size > i && path[i] == target.path[i]
        }
        return isTarget
    }

    override fun toString(): String {
        val joiner = StringJoiner(".")
        for (s in path) joiner.add(s)
        return joiner.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PackageTarget

        if (path != other.path) return false

        return true
    }

    override fun hashCode(): Int {
        return path.hashCode()
    }

    companion object {
        @JvmStatic
        fun of(path: String): PackageTarget {
            return PackageTarget(fromPath(path))
        }

        @JvmStatic
        fun of(cls: Class<*>): PackageTarget {
            return PackageTarget(fromPath(cls.getPackage().name))
        }

        @JvmStatic
        protected fun fromPath(path: String): List<String> {
            return path.split(".")
        }
    }
}