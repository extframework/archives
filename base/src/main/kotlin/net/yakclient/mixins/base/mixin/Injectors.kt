package net.yakclient.mixins.base.mixin

import net.yakclient.mixins.base.ByteCodeUtils
import net.yakclient.mixins.base.InstructionAdapters
import net.yakclient.mixins.base.InstructionResolver
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*

object Injectors {
    val BEFORE_END: MixinInjectionPoint = {
        var lastReturn: AbstractInsnNode? = null
        for (node in insn) {
            if (ByteCodeUtils.isReturn(node.opcode)) lastReturn = node
        }

        checkNotNull(lastReturn) { "Failed to find last return in given instructions." }

        listOf(BeforeInsnNodeInjector(insn, lastReturn))
    }

    val AFTER_BEGIN: MixinInjectionPoint = { listOf(BeginningInjector(insn)) }

    val BEFORE_INVOKE: MixinInjectionPoint =
        { insn.filterIsInstance<MethodInsnNode>().map { BeforeInsnNodeInjector(insn, it) } }

    val BEFORE_RETURN: MixinInjectionPoint = {
        insn.filter { ByteCodeUtils.isReturn(it.opcode) }.map { BeforeInsnNodeInjector(insn, it) }
    }

    val OPCODE_MATCHER : MixinInjectionPoint = { opcode ->
        insn.filter { it.opcode == opcode }.map { BeforeInsnNodeInjector(insn, it) }
    }

    val OVERWRITE: MixinInjectionPoint = {
        listOf(MixinInjector { toInject -> node.instructions = toInject.get() })
    }

    abstract class MixinAdaptedInjector(
        protected val insn: InsnList
    ) : MixinInjector {
        override fun inject(toInject: InstructionResolver) =
            inject(InstructionAdapters.RemoveLastReturn(toInject).get())

        abstract fun inject(toInject: InsnList)
    }

    class BeforeInsnNodeInjector(
        insn: InsnList,
        private val node: AbstractInsnNode,
    ) : MixinAdaptedInjector(insn) {
        init {
            require(insn.contains(node)) { "Given InsnList does not contain the node provided to inject before." }
        }

        override fun inject(toInject: InsnList) = insn.insertBefore(node, toInject)
    }

    class BeginningInjector(
        insn: InsnList,
    ) : MixinAdaptedInjector(insn) {
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