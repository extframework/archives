package net.yakclient.mixins.base.internal.instruction.tree

import net.yakclient.mixins.base.internal.instruction.Instruction
import net.yakclient.mixins.base.internal.instruction.InstructionExecutor
import org.objectweb.asm.tree.InsnList

class InitialTreeInsnExecutor(private val insnList: InsnList, val insn: Instruction) : InstructionExecutor {
    override fun execute() {
        insnList.insert(insn.insn)
    }
}