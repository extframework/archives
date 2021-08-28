package net.yakclient.mixins.base.internal.instruction

import org.objectweb.asm.tree.InsnList

interface Instruction {
    val insn: InsnList
}