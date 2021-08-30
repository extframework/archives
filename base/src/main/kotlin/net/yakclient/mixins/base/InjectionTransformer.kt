package net.yakclient.mixins.base

import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.MethodNode

interface InjectionTransformer<T> : (T) -> T {
    override fun invoke(context: T): T
}

interface ClassTransformer : InjectionTransformer<ClassNode>

interface MethodTransformer : InjectionTransformer<MethodNode>

interface FieldTransformer : InjectionTransformer<FieldNode>