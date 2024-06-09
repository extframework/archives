package dev.extframework.archives.transform

import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldNode
import org.objectweb.asm.tree.MethodNode

/**
 * The base transformer type for all injection transformers.
 *
 * @param T the context that needs to be transformed.
 *
 * @since 1.1-SNAPSHOT
 * @author Durgan McBroom
 */
public fun interface InjectionTransformer<T> : (T) -> T {
    override fun invoke(context: T) : T
}



/**
 * The base transformer type for all class transformations.
 *
 * @since 1.1-SNAPSHOT
 * @author Durgan McBroom
 */
public typealias ClassTransformer = InjectionTransformer<ClassNode>
/**
 * The base transformer type for all method transformations.
 *
 * @since 1.1-SNAPSHOT
 * @author Durgan McBroom
 */
public typealias MethodTransformer = InjectionTransformer<MethodNode>

/**
 * The base transformer for all field transformations.
 *
 * @since 1.1-SNAPSHOT
 * @author Durgan McBroom
 */
public typealias FieldTransformer = InjectionTransformer<FieldNode>