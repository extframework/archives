package net.yakclient.mixins.base

import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldNode
import org.objectweb.asm.tree.MethodNode

sealed class ProxiedTransformer<C>(
    private val transformers: List<InjectionTransformer<C>>
) {
    protected fun transformAll(context: C): C {
        var last = context
        transformers.forEach { last = it(last) }
        return last
    }
}

class ProxiedMethodTransformer(
    transformers: List<MethodTransformer>
) : ProxiedTransformer<MethodNode>(transformers), MethodTransformer {
    override fun invoke(context: MethodNode): MethodNode = transformAll(context)
}

class ProxiedClassTransformer(
    transformers: List<ClassTransformer>

) : ProxiedTransformer<ClassNode>(transformers), ClassTransformer {
    override fun invoke(context: ClassNode): ClassNode = transformAll(context)
}

class ProxiedFieldTransformer(
    transformers: List<FieldTransformer>
) : ProxiedTransformer<FieldNode>(transformers), FieldTransformer {
    override fun invoke(context: FieldNode): FieldNode = transformAll(context)
}