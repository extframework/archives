package net.yakclient.mixin.base.internal.loader

import net.yakclient.mixin.base.internal.bytecode.ByteCodeUtils.classExists
import java.util.*

class ClassTarget(path: List<String>, private val cls: String) : PackageTarget(path) {
    fun toPackage(): PackageTarget {
        return PackageTarget(path)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        if (!super.equals(other)) return false
        val that = other as ClassTarget
        return cls == that.cls
    }

    override fun hashCode(): Int {
        return Objects.hash(super.hashCode(), cls)
    }

    override fun toString(): String {
        return super.toString() + "." + cls
    }

    companion object {
        @JvmStatic
       fun of(path: String): ClassTarget {
            return try {
                if (classExists(path)) {
                    val fromPath: List<String> = fromPath(path)
                    val i = fromPath.size - 1
                    val split = fromPath.subList(0, i);
//                    val split = arrayOfNulls<String>(i)
//                    System.arraycopy(fromPath, 0, split, 0, i)
                    ClassTarget(split, fromPath[i])
                } else throw ClassNotFoundException("Failed to find specified class")
            } catch (e: ClassNotFoundException) {
                throw IllegalArgumentException("Invalid class path")
            }
        }
        @JvmStatic

        fun of(path: Class<*>): ClassTarget {
            val fromPath: List<String> = fromPath(path.name)
            val i = fromPath.size - 1
            val split = fromPath.subList(0, i);
            return ClassTarget(split, fromPath[i])
        }
    }
}