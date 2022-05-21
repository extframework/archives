package net.yakclient.archives.mixin

import net.yakclient.archives.transform.InstructionAdapter
import net.yakclient.archives.transform.InstructionResolver
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.FieldInsnNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.MethodInsnNode

public object InstructionAdapters {
    public class RemoveLastReturn(
        parent: InstructionResolver
    ) : InstructionAdapter(parent) {
        override fun get(): InsnList = super.get().also { insn ->
            val lastReturn: AbstractInsnNode? = insn.lastOrNull { it.opcode == Opcodes.RETURN }

            if (lastReturn != null) insn.remove(lastReturn)
        }
    }

    public class AlterThisReference(
        parent: InstructionResolver,
        private val clsTo: String,
        private val clsFrom: String
    ) : InstructionAdapter(parent) {
        override fun get(): InsnList = super.get().also {
            for (insn in it) {
                if (insn is MethodInsnNode && insn.opcode != Opcodes.INVOKESTATIC && insn.owner.equals(this.clsFrom)) insn.owner = this.clsTo

                if (insn is FieldInsnNode && insn.opcode != Opcodes.GETSTATIC && insn.owner.equals(this.clsFrom)) insn.owner = this.clsTo
            }
        }
    }
}