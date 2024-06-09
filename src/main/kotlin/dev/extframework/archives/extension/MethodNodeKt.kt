@file:JvmName("MethodNodeKt")

package dev.extframework.archives.extension

import org.objectweb.asm.commons.Method
import org.objectweb.asm.tree.MethodNode

///**
// * Returns a list of parameters in class form.
// *
// * @receiver The node to find the parameters of.
// *
// * @return the parameters.
// */
//public fun MethodNode.parameters(): List<Class<*>> = parameterClasses(this.desc)

///**
// * Turns a bytecode description into a list of classes. The
// * form of the description should be as follows:
// * `( + <description> + )`
// *
// * An actual example:
// * ```
// * (ZC[I[ILjava/lang/Object;D)
// * ```
// *
// * @param desc the description to find the parameters of.
// *
// * @return the list of classes.
// */
//public fun parameterClasses(
//    desc: String,
//    classloader: (String) -> Class<*>
//    = {
//        Class.forName(it)
//        // it.firstOrNull()?.let(ByteCodeUtils::primitiveType) ?:
//    }
//): List<Class<*>> = parameters(desc)
//    .map {
//        if (it.startsWith("L") && it.endsWith(";")) classloader(
//            it.removePrefix("L").removeSuffix(";").replace('/', '.')
//        )
//        else if (it.startsWith("[")) classloader(it.replace('/', '.'))
//        else if (ByteCodeUtils.isPrimitiveType(it.first())) ByteCodeUtils.primitiveType(it.first())!!
//        else throw IllegalArgumentException("Unknown parameter type: '$it' in parameters '$desc'")
//    }

///**
// * Complies with javas method SIGNATURE specification. This is a superset of the Java method
// * Descriptor so this method will work with method descriptors awell.
// */
//public fun parameters(signature: String): List<String> = listOf {
//    fun <E : Enum<E>> E.or(vararg type: Enum<E>): Boolean = type.any { equals(it) }
//
//    fun StringBuilder.addType() =
//        this.toString().also { add(it); this.clear() }
//
//    val builder = StringBuilder()
//    val type = Stack<SearchType>()
//    type.push(NONE)
//
//    for (c in signature.trim('(', ')')) {
//        builder.append(c)
//        val peek = type.peek()
//        if (c == 'L' && peek.or(NONE, GENERIC)) type.push(OBJECT)
//        else if (c == 'L' && peek == ARRAY_NOT_DETERMINED) {
//            type.pop()
//            type.push(OBJECT)
//        } else if (c == '[' && peek.or(NONE, ARRAY_NOT_DETERMINED)) {
//            if (peek == NONE)
//            type.push(ARRAY_NOT_DETERMINED)
//        } else if (c == '<' && peek == OBJECT) {
//            type.push(GENERIC)
//        } else if (c == '>' && peek == GENERIC) {
//            type.pop()
//        } else if (c == ';' && peek == OBJECT) {
//            type.pop()
//            if (type.peek() == NONE)
//                builder.addType()
//        } else {
//            val isPrimitive = ByteCodeUtils.isPrimitiveType(c)
//            if (isPrimitive && peek == ARRAY_NOT_DETERMINED) {
//                type.pop()
//                builder.addType()
//            } else if (isPrimitive && peek == NONE) builder.addType()
//        }
//    }
//}
//
//
////private data class ParameterType(
////    val type: SearchType,
////    val innerType:
////)
//
//private enum class SearchType {
//    OBJECT,
//    ARRAY_OBJECT,
//    ARRAY_NOT_DETERMINED,
//    GENERIC,
//    NONE
//}

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
public fun MethodNode.sameSignature(method: String): Boolean =
    Method(this.name, this.desc).overloads(Method(method))

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
public fun MethodNode.sameSignature(signature: Method): Boolean =
    Method(this.name, this.desc).overloads(signature)


public fun MethodNode.toMethod() : Method {
    return Method(name, desc)
}
//private fun <T> listOf(builder: MutableList<T>.() -> Unit): List<T> = ArrayList<T>().apply(builder).toList()

