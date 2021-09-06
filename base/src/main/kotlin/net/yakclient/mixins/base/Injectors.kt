package net.yakclient.mixins.base

import net.yakclient.mixins.base.internal.bytecode.ByteCodeUtils
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.MethodInsnNode

object Injectors {
    val BEFORE_END = MixinInjectionPoint { insn ->
        var lastReturn: AbstractInsnNode? = null
        for (node in insn) {
            if (ByteCodeUtils.isReturn(node.opcode)) lastReturn = node
        }

        checkNotNull(lastReturn) { "Failed to find last return in given instructions." }

        listOf(BeforeInsnNodeInjector(lastReturn, insn))
    }

    val AFTER_BEGIN = MixinInjectionPoint { insn -> listOf(BeginningInjector(insn)) }

    val BEFORE_INVOKE = MixinInjectionPoint { insn -> insn.filterIsInstance<MethodInsnNode>().map { BeforeInsnNodeInjector(it, insn) } }

    val BEFORE_RETURN = MixinInjectionPoint { insn -> insn.filter { ByteCodeUtils.isReturn(it.opcode) }.map { BeforeInsnNodeInjector(it, insn) } }

    val OPCODE_MATCHER = OpcodeInjectionPoint()

    //Contract not pure
    val OVERWRITE = MixinInjectionPoint { it.clear(); listOf(BeginningInjector(it)) }

    class OpcodeInjectionPoint : MixinInjectionPoint {
        override fun find(insn: InsnList) = find(insn, -1)

        fun find(insn: InsnList, opcode: Int): List<MixinInjector> = insn.filter { it.opcode == opcode }.map { BeforeInsnNodeInjector(it, insn) }
    }

    class BeforeInsnNodeInjector(
        private val node: AbstractInsnNode,
        list: InsnList
    ) : MixinInjector(list) {
        init {
            require(list.contains(node)) { "Given InsnList does not contain the node provided to inject before." }
        }

        override fun inject(toInject: InsnList) = insn.insertBefore(node, toInject)
    }

    class BeginningInjector(
        list: InsnList
    ) : MixinInjector(list) {
        override fun inject(toInject: InsnList) = insn.insert(toInject)
    }

    fun of(id: Int) = when (id) {
        200 -> AFTER_BEGIN
        201 -> BEFORE_END
        202 -> BEFORE_RETURN
        203 -> BEFORE_INVOKE
        204 -> OVERWRITE
        else -> OPCODE_MATCHER
    }
}