@file:JvmName("ClassNodeKt")

package dev.extframework.archives.extension

import org.objectweb.asm.commons.Method
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldNode
import org.objectweb.asm.tree.MethodNode

/**
 * Returns the given MethodNode matching the provided name and parameter
 * arguments. If not found then null will be returned.
 *
 * @receiver the `ClassNode` to find the method of.
 *
 * @param method the method object ot check against
 *
 * @return the found method node or null
 */
public fun ClassNode.methodOf(method: Method): MethodNode? = methods.find {
    val itMethod = it.toMethod()
    itMethod.name == method.name && itMethod.argumentTypes.contentEquals(method.argumentTypes)
            && itMethod.returnType == method.returnType
}

public fun ClassNode.hasMethod(method: Method): Boolean = methodOf(method) != null

/**
 * Returns the given FieldNode matching the provided name. If not found then
 * null will be returned.
 *
 * @receiver The `ClassNode` to find the field of.
 *
 * @param name the name to match
 *
 * @return the found node or null.
 */
public fun ClassNode.fieldOf(name: String): FieldNode? = fields.find { it.name == name }

