package net.yakclient.mixin.base.internal.instruction.adapter

import net.yakclient.mixin.base.internal.instruction.Instruction
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.FieldInsnNode

class FieldSelfInsnAdapter : InsnAdapter {
    private val clsTo: String
    private val clsFrom: String

    constructor(adapter: InsnAdapter?, clsTo: String, clsFrom: String) : super(adapter) {
        this.clsTo = clsTo.replace(".", "/")
        this.clsFrom = clsFrom.replace(".", "/")
    }

    override fun adapt(instruction: Instruction): Instruction {
        for (insn in instruction.insn) {
            if (insn is FieldInsnNode && insn.getOpcode() != Opcodes.GETSTATIC && insn.owner == clsFrom) insn.owner =
                clsTo
        }
        return super.adapt(instruction)
    }
}