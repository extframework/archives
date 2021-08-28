package net.yakclient.mixins.base.registry.pool

import java.net.URL

interface Location

open class ClassLocation(val cls: String) : Location {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ClassLocation

        if (cls != other.cls) return false

        return true
    }

    override fun hashCode(): Int {
        return cls.hashCode()
    }
}

fun mlSources(data: MixinMetaData): MethodLocation {
    return MethodLocation(data.classFrom, data.methodFrom, data.type, data.priority)
}

fun mlDestination(data: MixinMetaData): MethodLocation {
    return MethodLocation(data.classTo, data.methodTo, data.type, data.priority)
}


class MethodLocation(
    cls: String,
    val method: String,
    val injectionType: Int,
    val priority: Int
) : ClassLocation(cls) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as MethodLocation

        if (method != other.method) return false
        if (injectionType != other.injectionType) return false
        if (priority != other.priority) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + method.hashCode()
        result = 31 * result + injectionType
        result = 31 * result + priority
        return result
    }
}

data class ExternalLibLocation(val url: URL) : Location