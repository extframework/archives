package net.yakclient.mixins.base

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.InsnList

object InstructionAdapters {
    class RemoveLastReturn(
        parent: InstructionResolver
    ) : InstructionAdapter(parent) {
        override fun get(): InsnList = super.get().also { insn ->
            val lastReturn: AbstractInsnNode? = insn.lastOrNull { it.opcode == Opcodes.RETURN }

            if (lastReturn != null) insn.remove(lastReturn)
        }
    }
}