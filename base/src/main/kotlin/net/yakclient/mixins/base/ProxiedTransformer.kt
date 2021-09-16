package net.yakclient.mixins.base

import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldNode
import org.objectweb.asm.tree.MethodNode

/**
 * Using the provided transformers transforms the given context with
 * the output of the last transformer as the input of the next. This
 * is the abstract implementation of a `ProxiedTransformer`,
 * implementations will define specific transformative operations.
 *
 * @param C the context type to transform.
 * @constructor Constructs with the given transformers.
 *
 * @see InjectionTransformer
 *
 * @since 1.1-SNAPSHOT
 * @author Durgan McBroom
 */
public sealed class ProxiedTransformer<C>(
    private val transformers: Collection<InjectionTransformer<C>>
) {
    /**
     * Calls all transformers provided on construction with the give
     * context.
     *
     * @param context the context to transform.
     * @return the transformed context.
     */
    protected fun transformAll(context: C): C {
        var last = context
        transformers.forEach { last = it(last) }
        return last
    }
}

/**
 * Conducts proxied transformations on methods.
 *
 * @see MethodTransformer
 *
 * @since 1.1-SNAPSHOT
 */
public class ProxiedMethodTransformer(
    transformers: List<MethodTransformer>
) : ProxiedTransformer<MethodNode>(transformers), MethodTransformer {
    override fun invoke(context: MethodNode): MethodNode = transformAll(context)
}

/**
 * Conducts proxied transformations on classes.
 *
 * @see ClassTransformer
 *
 * @since 1.1-SNAPSHOT
 */
public class ProxiedClassTransformer(
    transformers: List<ClassTransformer>

) : ProxiedTransformer<ClassNode>(transformers), ClassTransformer {
    override fun invoke(context: ClassNode): ClassNode = transformAll(context)
}

/**
 * Conducts proxied transformations on fields.
 *
 * @see FieldTransformer
 *
 * @since 1.1-SNAPSHOT
 */
public class ProxiedFieldTransformer(
    transformers: List<FieldTransformer>
) : ProxiedTransformer<FieldNode>(transformers), FieldTransformer {
    override fun invoke(context: FieldNode): FieldNode = transformAll(context)
}