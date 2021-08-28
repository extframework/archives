package net.yakclient.mixins.base

import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.MethodNode

interface InjectionResolver<T> : (T) -> T? {
    override fun invoke(context: T): T?

    companion object {
        fun <C, R : InjectionResolver<C>> resolveAll(context: C, resolvers: Collection<R>): C {
            var last: C = context
            for (resolver in resolvers) {
                last = resolver(last) ?: continue
            }
            return last
        }
    }
}

abstract class ClassResolver(
    protected val methodResolvers: List<MethodResolver>,
    protected val fieldResolvers: List<FieldResolver>
) : InjectionResolver<ClassNode> {
    override fun invoke(context: ClassNode): ClassNode = context.apply { }
}

abstract class MethodResolver(
    protected val instructionResolvers: List<InstructionResolver>
) : InjectionResolver<MethodNode> {
    override fun invoke(context: MethodNode): MethodNode? =
        context.apply { instructions = InjectionResolver.resolveAll(instructions, instructionResolvers) }
}

interface InstructionResolver : InjectionResolver<InsnList>

interface FieldResolver : InjectionResolver<FieldNode>