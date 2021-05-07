package net.yakclient.mixin.base.registry.pool

import java.util.*

open class MethodLocation(cls: String, val method: String) : ClassLocation(cls) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        if (!super.equals(other)) return false
        val that = other as MethodLocation
        return method == that.method
    }

    override fun hashCode(): Int {
        return Objects.hash(super.hashCode(), method)
    }

    companion object {
        fun fromDataDest(data: MixinMetaData): MethodLocation {
            return MethodLocation(data.classTo, data.methodTo)
        }

        fun fromDataOrigin(data: MixinMetaData): MethodLocation {
            return MethodLocation(data.classFrom, data.methodFrom)
        }
    }
}