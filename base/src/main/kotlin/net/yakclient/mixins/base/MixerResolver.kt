package net.yakclient.mixins.base

import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.MethodNode

class MixerResolver(
    private val method: String
) : MethodResolver(listOf()) {
    override fun invoke(context: MethodNode): MethodNode? =
        if (context.name + context.desc != method) null
        else super.invoke(context)
}

class MixinInjectionResolver(
    private val point: MixinInjectionPoint,
    private val source: InsnList
) : InstructionResolver() {
    override fun invoke(context: InsnList): InsnList = context.apply {
        point.find(context).forEach {
            context.insert(it, source)
        }
    }
}

interface MixinInjectionPoint {
    fun find(insn: InsnList): List<AbstractInsnNode>
}

