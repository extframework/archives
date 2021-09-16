package net.yakclient.mixins.base.mixin

import net.yakclient.mixins.base.InstructionResolver
import net.yakclient.mixins.base.MethodTransformer
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.MethodNode

internal class MixinInjectionTransformer(
    private val point: MixinInjectionPoint,
    private val opcode: Int = -1,
    private val source: InstructionResolver
) : MethodTransformer {
    override fun invoke(context: MethodNode): MethodNode = context.apply {
        point.apply(context, opcode).forEach { it.inject(source) }
    }
}

internal typealias MixinInjectionPoint = MixinInjectionContext.(opcode: Int) -> List<MixinInjector>

internal class MixinInjectionContext(
    val node: MethodNode
) {
    val insn : InsnList = node.instructions
}
private fun MixinInjectionPoint.apply(node: MethodNode, opcode: Int) : List<MixinInjector> = this(MixinInjectionContext(node), opcode)

internal fun interface MixinInjector {
    fun inject(toInject: InstructionResolver)
}
