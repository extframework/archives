package net.yakclient.mixins.base.internal.instruction.tree

import net.yakclient.mixins.base.internal.instruction.Instruction
import net.yakclient.mixins.base.internal.instruction.InstructionExecutor
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.InsnList

class PlacedTreeInsnExecutor(private val insnList: InsnList, private val index: AbstractInsnNode, val insn: Instruction) :
    InstructionExecutor {
    override fun execute() {
        insnList.insertBefore(index, insn.insn)
    }
}