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

class MixinInjectionResolver(
    private val point: MixinInjectionPoint,
    private val opcode : Int = -1,
    private val source: InsnList
) : MethodTransformer {
    override fun invoke(context: MethodNode): MethodNode = context.apply {
        val insn = context.instructions
       (if (opcode != -1) (point as Injectors.OpcodeInjectionPoint).find(insn, opcode) else point.find(insn)).forEach {
            insn.insert(it, source)
        }
    }
}

fun interface MixinInjectionPoint {
    fun find(insn: InsnList): List<AbstractInsnNode>
}

