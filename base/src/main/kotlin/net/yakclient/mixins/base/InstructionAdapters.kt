package net.yakclient.mixins.base

import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.InsnList

object InstructionAdapters {
//    val REMOVE_LAST_RETURN = object : InstructionAdapter() {
//        override fun get(): InsnList {
//            insn.remove(Injectors.BEFORE_END.find(insn).first())
//            return super.filter(insn)
//        }
//    }

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

    class RemoveNonPositiveOpcodes(
        parent: InstructionResolver
    ) : InstructionAdapter(parent) {
        override fun get(): InsnList = super.get().apply { removeAll { it.opcode == -1 } }
    }


}