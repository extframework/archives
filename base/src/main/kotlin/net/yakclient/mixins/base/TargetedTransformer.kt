package net.yakclient.mixins.base

import net.yakclient.mixins.base.extension.sameSignature
import org.objectweb.asm.tree.FieldNode
import org.objectweb.asm.tree.MethodNode

/**
 * Transforms with the given delegate if the current context
 * satisfies the predicate provided.
 *
 * @constructor Constructs with the given Transformer
 *
 * @since 1.1-SNAPSHOT
 * @author Durgan McBroom
 */
public abstract class TargetedTransformer<T>(
   private val delegate: InjectionTransformer<T>,
) {
    /**
     * If the given context matches then calls the delegate, otherwise
     * returns the context provided.
     *
     * @param c The context to match
     * @return The context(transformed or not)
     */
    public fun call(c: T): T =
        if (matches(c)) delegate(c) else c

    /**
     * Determines if the given context matches.
     *
     * @param c the context to match
     * @return If the context matches.
     */
    public abstract fun matches(c: T): Boolean
}

/**
 * Targets methods based on the provided signature.
 *
 * @constructor Constructs with the given signature and delegate.
 *
 * @since 1.1-SNAPSHOT
 * @author Durgan McBroom
 */
public class TargetedMethodTransformer(
    delegate: MethodTransformer,
    private val signature: ByteCodeUtils.MethodSignature,
) : TargetedTransformer<MethodNode>(delegate), MethodTransformer {
    override fun invoke(context: MethodNode): MethodNode = call(context)

    override fun matches(c: MethodNode): Boolean = c.sameSignature(signature)
}

/**
 * Targets fields based on the name provided.
 *
 * @constructor Constructs with the given name and delegate.
 *
 * @since 1.1-SNAPSHOT
 * @author Durgan McBroom
 */
public class TargetedFieldTransformer(
    delegate: FieldTransformer,
    private val name: String,
) : TargetedTransformer<FieldNode>(delegate), FieldTransformer {
    override fun invoke(context: FieldNode): FieldNode = call(context)

    override fun matches(c: FieldNode): Boolean = c.name == name
}