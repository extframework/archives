package net.yakclient.mixins.base.mixin

import net.yakclient.mixins.base.ByteCodeUtils
import net.yakclient.mixins.base.InstructionResolver
import net.yakclient.mixins.base.MethodTransformer
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.MethodNode

class MixinInjectionTransformer(
    private val point: MixinInjectionPoint,
    private val opcode: Int = -1,
    private val source: InstructionResolver
) : MethodTransformer {
    override fun invoke(context: MethodNode): MethodNode = context.apply {

//        ByteCodeUtils.insnToString(context.instructions).forEach { println(it) }
        point.apply(context, opcode).forEach { it.inject(source) }

//        println("----------------------")

//        ByteCodeUtils.insnToString(context.instructions).forEach { println(it) }
    }
}

typealias MixinInjectionPoint = MixinInjectionContext.(opcode: Int) -> List<MixinInjector>

class MixinInjectionContext(
    val node: MethodNode
) {
    val insn : InsnList = node.instructions
}
private fun MixinInjectionPoint.apply(node: MethodNode, opcode: Int) : List<MixinInjector> = this(MixinInjectionContext(node), opcode)


//private fun MixinInjectionPoint.apply(node: MethodNode) : List<MixinInjector> = this(MixinInjectionContext(node))
//{
//    protected val instructions = node.instructions
//}
//fun interface MixinInjectionPoint {
//    fun find(context: MixinInjectionContext): List<MixinInjector>
//
////    fun find(node: MethodNode) = find(MixinInjectionContext(node))
//
//
//}

fun interface MixinInjector {
    fun inject(toInject: InstructionResolver)
}
