package net.yakclient.mixin.base.internal.instruction.adapter

import net.yakclient.mixin.base.internal.instruction.Instruction
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.AbstractInsnNode

class ReturnRemoverInsnAdapter : InsnAdapter() {
    override fun adapt(instruction: Instruction): Instruction {
        val lr = recursivelyFindReturn(instruction.insn.last)
        val last = lr ?: instruction.insn.last
        if (last.opcode == Opcodes.RETURN) instruction.insn.remove(last)
        return super.adapt(instruction)
    }

    private fun recursivelyFindReturn(node: AbstractInsnNode): AbstractInsnNode? {
        if (node.opcode == Opcodes.RETURN) return node
        return if (node.previous == null) null else recursivelyFindReturn(node.previous)
    }
}