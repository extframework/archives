@file:JvmName("MethodNodeKt")

package net.yakclient.archives.extension

import net.yakclient.archives.extension.SearchType.*
import net.yakclient.archives.transform.ByteCodeUtils
import net.yakclient.archives.transform.MethodSignature
import org.objectweb.asm.tree.MethodNode

/**
 * Returns a list of parameters in class form.
 *
 * @receiver The node to find the parameters of.
 *
 * @return the parameters.
 */
public fun MethodNode.parameters(): List<Class<*>> = parameterClasses(this.desc)

/**
 * Turns a bytecode description into a list of classes. The
 * form of the description should be as follows:
 * `( + <description> + )`
 *
 * An actual example:
 * ```
 * (ZC[I[ILjava/lang/Object;D)
 * ```
 *
 * @param desc the description to find the parameters of.
 *
 * @return the list of classes.
 */
public fun parameterClasses(
    desc: String,
    classloader: (String) -> Class<*> = { Class.forName(it) }
): List<Class<*>> = parameters(desc)
    .map {
//        fun unwrapPrimitive() : Class<*>? {
//
//        }
        (ByteCodeUtils.primitiveType(it.first())) ?: classloader(it)
    }

public fun parameters(desc: String) : List<String>  = listOf{
    fun <E : Enum<E>> E.or(vararg type: Enum<E>): Boolean = type.any { equals(it) }

    fun StringBuilder.containedClass(): String =
        this.toString().replace('/', '.').also { this.clear() }

    val builder = StringBuilder()
    var type = NONE

    for (c in desc.trim('(', ')')) {
        if (c == 'L' && type == NONE) type = OBJECT
        else if (c == 'L' && type == ARRAY_NOT_DETERMINED) {
            type = ARRAY_OBJECT
            builder.append(c)
        } else if (c == '[' && type.or(NONE, ARRAY_NOT_DETERMINED)) {
            type = ARRAY_NOT_DETERMINED
            builder.append(c)
        } else if (c == ';' && type.or(OBJECT, ARRAY_OBJECT)) {
            if (type == ARRAY_OBJECT) builder.append(c)
            type = NONE
            add(builder.containedClass())
        } else {
            val isPrimitive = ByteCodeUtils.isPrimitiveType(c)
            if (isPrimitive && type == ARRAY_NOT_DETERMINED) {
                type = NONE
                builder.append(c)
                add(builder.containedClass())
            } else if (isPrimitive && type == NONE) add(c.toString())
            else builder.append(c)
        }
    }
}

/**
 * Determines if the given signature is the same as the receiver
 * this is called on.
 *
 * @receiver the node to determine the signature of.
 *
 * @param signature the signature to match
 *
 * @return if the two signatures match
 */
public fun MethodNode.sameSignature(signature: String): Boolean =
    ByteCodeUtils.sameSignature(name + desc, signature)

/**
 * Determines if the given bytecode signature is the same as the receiver
 * this is called on.
 *
 * @receiver the node to determine the signature of.
 *
 * @param signature the signature to match
 *
 * @return if the two signatures match
 */
public fun MethodNode.sameSignature(signature: MethodSignature): Boolean =
    MethodSignature.of(name + desc).matches(signature)

private fun <T> listOf(builder: MutableList<T>.() -> Unit): List<T> = ArrayList<T>().apply(builder).toList()

private enum class SearchType {
    OBJECT,
    ARRAY_OBJECT,
    ARRAY_NOT_DETERMINED,
    NONE
}
