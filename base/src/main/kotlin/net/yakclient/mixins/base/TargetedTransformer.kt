package net.yakclient.mixins.base

import net.yakclient.mixins.base.extension.sameSignature
import org.objectweb.asm.tree.FieldNode
import org.objectweb.asm.tree.MethodNode

abstract class TargetedTransformer<T>(
   private val delegate: InjectionTransformer<T>,
) {
    fun call(c: T): T =
        if (matches(c)) delegate(c) else c

    abstract fun matches(c: T): Boolean
}

class TargetedMethodTransformer(
    delegate: MethodTransformer,
    private val signature: String,
) : TargetedTransformer<MethodNode>(delegate), MethodTransformer {

    override fun matches(c: MethodNode): Boolean = c.sameSignature(signature)

    override fun invoke(context: MethodNode): MethodNode = call(context)
}

class TargetedFieldTransformer(
    delegate: FieldTransformer,
    private val name: String,
) : TargetedTransformer<FieldNode>(delegate), FieldTransformer {
    override fun invoke(context: FieldNode): FieldNode = call(context)

    override fun matches(c: FieldNode): Boolean = c.name == name
}