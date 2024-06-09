package dev.extframework.archives.extension

import org.objectweb.asm.tree.AbstractInsnNode
import java.util.function.Predicate

public fun interface InstructionMatcher : Predicate<AbstractInsnNode>

internal open class TypedInsnMatcher(
    private val type: Class<out AbstractInsnNode>
) : InstructionMatcher {
    override fun test(t: AbstractInsnNode): Boolean = type.isAssignableFrom(t::class.java)
}

internal class TypedPredicateInsnMatcher<T : AbstractInsnNode>(
    type: Class<T>,
    private val predicate: Predicate<T>
) : TypedInsnMatcher(type) {
    override fun test(t: AbstractInsnNode): Boolean =
        super.test(t) && predicate.test(t as T)
}

internal class OpcodeInsnMatcher(
    private val opcode: Int
) : InstructionMatcher {
    override fun test(t: AbstractInsnNode): Boolean = t.opcode == opcode
}