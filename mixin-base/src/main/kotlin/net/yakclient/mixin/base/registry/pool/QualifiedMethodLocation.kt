package net.yakclient.mixin.base.registry.pool

import java.util.*

class QualifiedMethodLocation(cls: String, method: String, val injectionType: Int, val priority: Int) :
    MethodLocation(cls, method) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as QualifiedMethodLocation
        return priority == that.priority && injectionType == that.injectionType
    }

    override fun hashCode(): Int {
        return Objects.hash(injectionType, priority)
    }

    companion object {
        fun fromDataDest(data: MixinMetaData): QualifiedMethodLocation {
            return QualifiedMethodLocation(data.classTo, data.methodTo, data.type, data.priority)
        }

        fun fromDataOrigin(data: MixinMetaData): QualifiedMethodLocation {
            return QualifiedMethodLocation(
                data.classFrom,
                data.methodFrom,
                data.type,
                data.priority
            )
        }
    }
}