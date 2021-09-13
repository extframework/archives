package net.yakclient.mixins.base

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.util.Textifier
import org.objectweb.asm.util.TraceMethodVisitor
import java.io.DataInputStream
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.reflect.Method
import java.util.function.Consumer

/**
 * A common set of utilities that might be needed for doing work with JVM
 * Bytecode.
 *
 * @since 1.0-SNAPSHOT
 * @author Durgan McBroom
 */
public object ByteCodeUtils {
    /**
     * Parses the runtime signature of the given method.
     *
     * @param method The method to parse.
     * @return the signature parsed.
     */
    public fun runtimeSignature(method: Method): String = "${method.name}${
        method.parameterTypes.joinToString(
            prefix = "(",
            postfix = ")"
        ) { primitiveType(it).toString() }
    }"


    /**
     * Returns the primitive type char for the given class. If the given class
     * is not a primitive then null is returned.
     *
     * @param cls The class to find the primitive type of.
     * @return The type char or null if it cannot be found.
     */
    public fun primitiveType(cls: Class<*>): Char? = when (cls) {
        Void.TYPE -> 'V'
        Boolean::class.javaPrimitiveType -> 'Z'
        Char::class.javaPrimitiveType -> 'C'
        Byte::class.javaPrimitiveType -> 'B'
        Short::class.javaPrimitiveType -> 'S'
        Int::class.javaPrimitiveType -> 'I'
        Float::class.javaPrimitiveType -> 'F'
        Long::class.javaPrimitiveType -> 'J'
        Double::class.javaPrimitiveType -> 'D'
        else -> null
    }

    /**
     * Takes a primitive char type and returns the corresponding class associated
     * with it, or null if the char is not a primitive type.
     *
     * @param c the char to find a primitive class of.
     * @return the primitive class or null.
     */
    public fun primitiveType(c: Char): Class<*>? = when (c) {
        'V' -> Void.TYPE
        'Z' -> Boolean::class.javaPrimitiveType
        'C' -> Char::class.javaPrimitiveType
        'B' -> Boolean::class.javaPrimitiveType
        'S' -> Short::class.javaPrimitiveType
        'I' -> Int::class.javaPrimitiveType
        'F' -> Float::class.javaPrimitiveType
        'J' -> Long::class.javaPrimitiveType
        'D' -> Double::class.javaPrimitiveType
        else -> null
    }


    /**
     * Parses the name of a given class into the runtime format.
     *
     * @sample net.example.ExampleClass becomes Lnet/example/ExampleClass;
     * @sample Void becomes V
     * @sample [net.example.ExampleClass becomes [Lnet/example/ExampleClass;
     *
     * @param type The class to get the runtime name of
     * @return the name.
     */
    public fun toRuntimeName(type: Class<*>): String = when {
        type.isPrimitive -> primitiveType(type).toString()
        type.isArray -> type.name
        else -> "L" + type.name.replace(
            '.',
            '/'
        ) + ";"
    }


    /**
     * Determines if the given char can be parsed to a primitive type.
     *
     * @param c the given char.
     * @return if the char can be parsed to a primitive.
     */
    public fun isPrimitiveType(c: Char): Boolean = when (c) {
        'V', 'Z', 'C', 'B', 'S', 'I', 'F', 'J', 'D' -> true
        else -> false
    }

    /**
     * Determines if the given opcode is a return instruction.
     *
     * @param opcode The opcode to check against.
     * @return If it is a return instruction or not.
     */
    public fun isReturn(opcode: Int): Boolean = when (opcode) {
        Opcodes.IRETURN, Opcodes.LRETURN, Opcodes.FRETURN, Opcodes.DRETURN, Opcodes.ARETURN, Opcodes.RETURN -> true
        else -> false
    }

    /**
     * Determines if the two strings have the same name and description, ignoring
     * the return type. Note, a return type does not have to be present at all for
     * this to work.
     *
     * @param first the first signature to match against.
     * @param second the second signature to match against.
     *
     * @return If the method signatures are the same.
     */
    public fun sameSignature(first: String, second: String): Boolean = MethodSignature.of(first)
        .matches(MethodSignature.of(second))

    /**
     * Represents a method signature and its 3 parts; A name, a description(parameters)
     * and a (optional) return type.
     *
     * @constructor Constructs a MethodSignature with the given name, description, and return type.
     *
     * @since 1.1-SNAPSHOT
     * @author Durgan McBroom
     */
    public data class MethodSignature(
        val name: String,
        val desc: String,
        val returnType: String?
    ) {
        /**
         * Checks if the name and description of the other Signature match this one.
         *
         * @param other The signature to match against.
         * @return if they match.
         */
        public fun matches(other: MethodSignature): Boolean =
            (other.name == name && other.desc == desc) && (if (returnType == null || other.returnType == null) true else returnType == other.returnType)

        public companion object {
            private const val NON_ARRAY_PATTERN = "[ZCBSIFJD]|(?:L.+;)"

            private const val ANY_VALUE_PATTERN = "$NON_ARRAY_PATTERN|(?:\\[+(?:$NON_ARRAY_PATTERN))"

            private const val SIGNATURE_PATTERN = "^(.+)\\(((?:$ANY_VALUE_PATTERN)*)\\)($ANY_VALUE_PATTERN|V)?$"

            /**
             * Parses a method signature into a MethodSignature.
             *
             * @param signature the unparsed signature to parse.
             * @return the signature if valid.
             *
             * @throws IllegalStateException If the given signature is invalid.
             */
            public fun of(signature: String): MethodSignature {
                val regex = Regex(SIGNATURE_PATTERN)
                check(regex.matches(signature)) { "Invalid method signature: $signature" }

                return regex.find(signature)!!.groupValues.let { values ->
                    MethodSignature(
                        values[1],
                        values[2],
                        values[3].takeIf { it.isNotBlank() }
                    )
                }
            }
        }
    }

    /**
     * Returns the runtime method signature of the provided method.
     *
     * @param method The method to parse the name of.
     * @return the parsed name.
     */
    public fun byteCodeSignature(method: Method): String {
        val methodReturn = method.returnType
        val builder = StringBuilder(method.name)
        builder.append('(')
        for (type in method.parameterTypes) {
            builder.append(
                when {
                    type.isPrimitive -> primitiveType(type)
                    type.isArray -> type.name
                    else -> "L" + type.name.replace(
                        '.',
                        '/'
                    ) + ";"
                }
            )
        }
        builder.append(')')
        builder.append(if (methodReturn.isPrimitive) primitiveType(methodReturn) else methodReturn.name)
        return builder.toString()
    }
}