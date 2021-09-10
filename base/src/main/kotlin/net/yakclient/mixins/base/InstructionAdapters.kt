package net.yakclient.mixins.base

import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.InsnList

object InstructionAdapters {
    class RemoveLastReturn(
        parent: InstructionResolver
    ) : InstructionAdapter(parent) {
        override fun get(): InsnList = super.get().also { insn ->
            var lastReturn: AbstractInsnNode? = null
            for (node in insn) {
                if (ByteCodeUtils.isReturn(node.opcode)) lastReturn = node
            }

            checkNotNull(lastReturn) { "Failed to find last return in given instructions." }

            insn.remove(lastReturn)
        }
    }
//
//    class RemoveNonPositiveOpcodes(
//        parent: InstructionResolver
//    ) : InstructionAdapter(parent) {
//        override fun get(): InsnList = super.get().apply { removeAll { it.opcode == -1 } }
//    }
}