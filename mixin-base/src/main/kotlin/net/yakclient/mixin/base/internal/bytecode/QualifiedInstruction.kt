package net.yakclient.mixin.base.internal.bytecode

import net.yakclient.mixin.base.internal.instruction.Instruction

class QualifiedInstruction(val priority: Int, val injectionType: Int, val insn: Instruction) :
    Comparable<QualifiedInstruction> {
    override fun compareTo(other: QualifiedInstruction): Int {
        return other.priority - priority
    }
}