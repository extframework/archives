package net.yakclient.mixins.base

import net.yakclient.mixins.base.extension.sameSignature
import org.objectweb.asm.tree.FieldNode
import org.objectweb.asm.tree.MethodNode

abstract class TargetedTransformer<T>(
    delegate: InjectionTransformer<T>,
) : DelegatingTransformer<T>(delegate) {
    override fun invoke(c: T): T =
        if (matches(c)) super.invoke(c) else c

    abstract fun matches(c: T): Boolean
}

class TargetedMethodTransformer(
    delegate: MethodTransformer,
    private val signature: String,
) : TargetedTransformer<MethodNode>(delegate) {
    override fun matches(c: MethodNode): Boolean = c.sameSignature(signature)
}

class TargetedFieldTransformer(
    delegate: FieldTransformer,
    private val name: String,
) : TargetedTransformer<FieldNode>(delegate) {
    override fun matches(c: FieldNode): Boolean = c.name == name
}