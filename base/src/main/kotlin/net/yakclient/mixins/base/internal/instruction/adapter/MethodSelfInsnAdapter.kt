package net.yakclient.mixins.base.internal.instruction.adapter

import net.yakclient.mixins.base.internal.instruction.Instruction
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.MethodInsnNode

class MethodSelfInsnAdapter : InsnAdapter {
    private val clsTo: String
    private val clsFrom: String

    constructor(adapter: InsnAdapter?, clsTo: String, clsFrom: String) : super(adapter) {
        this.clsTo = clsTo.replace(".", "/")
        this.clsFrom = clsFrom.replace(".", "/")
    }

    constructor(clsTo: String, clsFrom: String) {
        this.clsTo = clsTo
        this.clsFrom = clsFrom
    }

    override fun adapt(instruction: Instruction): Instruction {
        for (insn in instruction.insn) {
            if (insn is MethodInsnNode && insn.getOpcode() != Opcodes.INVOKESTATIC && insn.owner == clsFrom) insn.owner =
                clsTo
        }
        return super.adapt(instruction)
    }
}