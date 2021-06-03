package net.yakclient.mixin.base.registry.pool

import java.util.*

open class ClassLocation(val cls: String) : Location {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as ClassLocation
        return cls == that.cls
    }

    override fun hashCode(): Int {
        return Objects.hash(cls)
    }

    companion object {
        fun fromDataDest(data: MixinMetaData): ClassLocation {
            return ClassLocation(data.classTo)
        }

        fun fromDataOrigin(data: MixinMetaData): ClassLocation {
            return ClassLocation(data.classFrom)
        }
    }
}