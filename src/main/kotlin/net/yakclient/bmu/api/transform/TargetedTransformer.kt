package net.yakclient.bmu.api.transform

import net.yakclient.bmu.api.extension.sameSignature
import org.objectweb.asm.tree.ClassNode
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
) : InjectionTransformer<T> {
    /**
     * If the given context matches then calls the delegate, otherwise
     * returns the context provided.
     *
     * @param c The context to match
     * @return The context(transformed or not)
     */
    override fun invoke(context: T): T = if (matches(context)) delegate(context) else context

    /**
     * Determines if the given context matches.
     *
     * @param c the context to match
     * @return If the context matches.
     */
    public abstract fun matches(c: T): Boolean
}

public class TargetedClassTransformer(
    private val name: String,
    delegate: ClassTransformer
) : TargetedTransformer<ClassNode>(delegate) {
    override fun matches(c: ClassNode): Boolean = c.name == name.replace('.', '/')
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
    private val signature: MethodSignature,
    delegate: MethodTransformer,
) : TargetedTransformer<MethodNode>(delegate) {
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
    private val name: String,
    delegate: FieldTransformer,
) : TargetedTransformer<FieldNode>(delegate) {
    override fun matches(c: FieldNode): Boolean = c.name == name
}