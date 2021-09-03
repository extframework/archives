package net.yakclient.mixins.base

import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.MethodNode

fun interface InjectionTransformer<T> : (T) -> T {
    override fun invoke(context: T): T
}

fun interface ClassTransformer : InjectionTransformer<ClassNode>

fun interface MethodTransformer : InjectionTransformer<MethodNode>

fun interface FieldTransformer : InjectionTransformer<FieldNode>