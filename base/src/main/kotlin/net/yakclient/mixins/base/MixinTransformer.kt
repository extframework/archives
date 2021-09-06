package net.yakclient.mixins.base

import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.MethodNode

//class MixerResolver(
//    private val method: String
//) : MethodTransformer {
//    override fun invoke(context: MethodNode): MethodNode =
//        if (context.name + context.desc != method) context
//        else super.invoke(context)
//}

class MixinInjectionTransformer(
    private val point: MixinInjectionPoint,
    private val opcode: Int = -1,
    private val source: InstructionResolver
) : MethodTransformer {
    override fun invoke(context: MethodNode): MethodNode = context.apply {
        val insn = context.instructions
        (if (point is Injectors.OpcodeInjectionPoint) point.find(insn, opcode) else point.find(insn)).forEach {
            it.inject(source.get())
        }
    }
}

fun interface MixinInjectionPoint {
    fun find(insn: InsnList): List<MixinInjector>
}

sealed class MixinInjector(
    protected val insn: InsnList
) {
    abstract fun inject(toInject: InsnList)
}
