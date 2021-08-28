package net.yakclient.mixins.base

import net.yakclient.mixins.base.internal.bytecode.ByteCodeUtils
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.MethodInsnNode

object Injectors {
    val BEFORE_END = object : MixinInjectionPoint {
        override fun find(insn: InsnList): List<AbstractInsnNode> = listOf(
            checkNotNull(insn.find { ByteCodeUtils.isReturn(it.opcode) }) { "Failed to find end of instruction set." }
        )
    }

    val AFTER_BEGIN = object : MixinInjectionPoint {
        override fun find(insn: InsnList): List<AbstractInsnNode> = listOf(insn.first)
    }

    val BEFORE_INVOKE = object : MixinInjectionPoint {
        override fun find(insn: InsnList): List<AbstractInsnNode> = insn.filterIsInstance<MethodInsnNode>()
    }

    val BEFORE_RETURN = object : MixinInjectionPoint {
        override fun find(insn: InsnList): List<AbstractInsnNode> = insn.filter { ByteCodeUtils.isReturn(it.opcode) }
    }

    val OPCODE_MATCHER = object : MixinInjectionPoint {
        override fun find(insn: InsnList): List<AbstractInsnNode> = find(insn, -1)

        fun find(insn: InsnList, opcode: Int): List<AbstractInsnNode> = insn.filter { it.opcode == opcode }
    }
}