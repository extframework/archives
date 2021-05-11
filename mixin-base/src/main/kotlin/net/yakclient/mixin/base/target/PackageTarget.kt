package net.yakclient.mixin.base.target

import net.yakclient.mixin.base.internal.loader.ProxyClassLoader
import net.yakclient.mixin.base.internal.loader.TargetClassLoader
import java.util.*

open class PackageTarget(protected val path: List<String>): Target {
   override fun isTargetOf(target: Target): Boolean {
        if (!(target is PackageTarget)) return false;
        if (this == target) return true
        var isTarget = false
        for (i in path.indices) {
            isTarget = target.path.size > i && path[i] == target.path[i]
        }
        return isTarget
    }

    override fun name(): String {
        val joiner = StringJoiner(".")
        for (s in path) joiner.add(s)
        return joiner.toString()
    }

    override fun createLoader(parent: ClassLoader): ProxyClassLoader {
        return TargetClassLoader(parent, this)
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