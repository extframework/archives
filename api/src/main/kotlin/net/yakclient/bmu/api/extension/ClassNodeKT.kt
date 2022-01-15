@file:JvmName("ClassNodeKt")

package net.yakclient.bmu.api.extension

import net.yakclient.bmu.api.ByteCodeUtils
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldNode
import org.objectweb.asm.tree.MethodNode

/**
 * Returns the given MethodNode matching the provided name and parameter
 * arguments. If not found then null will be returned.
 *
 * @receiver the `ClassNode` to find the method of.
 *
 * @param name the name of the method to find.
 * @param args the parameters to match for.
 *
 * @return the found method node or null
 */
public fun ClassNode.methodOf(name: String, vararg args: Class<*>): MethodNode? = methods.find {
    it.name == name && args.joinToString(
        prefix = "(",
        postfix = ")",
        transform = ByteCodeUtils::toRuntimeName
    ) == it.desc
}

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

