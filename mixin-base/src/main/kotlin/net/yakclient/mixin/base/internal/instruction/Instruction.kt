package net.yakclient.mixin.base.internal.instruction

import org.objectweb.asm.tree.InsnList

interface Instruction {
    val insn: InsnList
}