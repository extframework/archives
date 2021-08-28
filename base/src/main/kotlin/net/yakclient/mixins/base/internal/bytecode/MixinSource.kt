package net.yakclient.mixins.base.internal.bytecode

import net.yakclient.mixins.base.registry.pool.MethodLocation
import java.util.*

open class MixinSource(val location: MethodLocation) {
    override fun toString(): String {
        return "MixinSource{" +
                "location=" + location +
                '}'
    }

    class MixinProxySource(location: MethodLocation, val pointer: UUID) : MixinSource(location) {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || javaClass != other.javaClass) return false
            val that = other as MixinProxySource
            return pointer == that.pointer
        }

        override fun hashCode(): Int {
            return Objects.hash(pointer)
        }

        override fun toString(): String {
            return "MixinProxySource{" +
                    "pointer=" + pointer +
                    '}'
        }
    }
}