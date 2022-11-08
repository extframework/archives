package net.yakclient.archives.mixin

import net.yakclient.archives.transform.InstructionResolver
import net.yakclient.archives.transform.MethodTransformer
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.MethodNode

internal class MixinInjectionTransformer(
    private val point: MixinInjectionPoint,
    private val source: InstructionResolver
) : MethodTransformer {
    override fun invoke(context: MethodNode): MethodNode = context.apply {
        point.apply(context).forEach { it.inject(source) }
    }
}

//internal typealias MixinInjectionPoint = MixinInjectionContext.() -> List<MixinInjector>

public data class MixinInjectionContext(
    public val node: MethodNode
) {
    public val insn : InsnList = node.instructions
}

private fun MixinInjectionPoint.apply(node: MethodNode) : List<MixinInjector> = apply(MixinInjectionContext(node))

public fun interface MixinInjector {
    public fun inject(toInject: InstructionResolver)
}
