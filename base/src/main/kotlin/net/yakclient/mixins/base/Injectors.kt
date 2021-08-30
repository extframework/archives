package net.yakclient.mixins.base

import net.yakclient.mixins.base.internal.bytecode.ByteCodeUtils
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.MethodInsnNode

object Injectors {
    val BEFORE_END = MixinInjectionPoint { insn ->
        listOf(checkNotNull(insn.find { ByteCodeUtils.isReturn(it.opcode) }) { "Failed to find end of instruction set." })
    }

    val AFTER_BEGIN = MixinInjectionPoint { insn -> listOf(insn.first) }

    val BEFORE_INVOKE = MixinInjectionPoint { insn -> insn.filterIsInstance<MethodInsnNode>() }

    val BEFORE_RETURN = MixinInjectionPoint { insn -> insn.filter { ByteCodeUtils.isReturn(it.opcode) } }

    val OPCODE_MATCHER = OpcodeInjectionPoint()

//        object : MixinInjectionPoint {
//        override fun find(insn: InsnList): List<AbstractInsnNode> = find(insn, -1)
//
//        fun find(insn: InsnList, opcode: Int): List<AbstractInsnNode> = insn.filter { it.opcode == opcode }
//    }

    class OpcodeInjectionPoint : MixinInjectionPoint {
        override fun find(insn: InsnList): List<AbstractInsnNode> = find(insn, -1)

        fun find(insn: InsnList, opcode: Int): List<AbstractInsnNode> = insn.filter { it.opcode == opcode }
    }
}