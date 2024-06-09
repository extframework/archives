package dev.extframework.archives.transform

import dev.extframework.archives.extension.Method
import dev.extframework.archives.extension.overloads
import dev.extframework.common.util.equalsAny
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.tree.FieldInsnNode
import org.objectweb.asm.tree.IincInsnNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.IntInsnNode
import org.objectweb.asm.tree.InvokeDynamicInsnNode
import org.objectweb.asm.tree.JumpInsnNode
import org.objectweb.asm.tree.LabelNode
import org.objectweb.asm.tree.LdcInsnNode
import org.objectweb.asm.tree.LineNumberNode
import org.objectweb.asm.tree.LookupSwitchInsnNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MultiANewArrayInsnNode
import org.objectweb.asm.tree.TableSwitchInsnNode
import org.objectweb.asm.tree.TypeInsnNode
import org.objectweb.asm.tree.VarInsnNode
import org.objectweb.asm.util.Printer
import java.lang.reflect.Method
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

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
            postfix = ")",
            separator = ""
        ) { primitiveType(it).toString() }
    }${toRuntimeName(method.returnType)}"

    public fun runtimeSignature(func: KFunction<*>): String = "${func.name}${
        func.parameters.filter { it.kind == KParameter.Kind.VALUE }.joinToString(
            prefix = "(",
            postfix = ")",
            separator = ""
        ) {
            toRuntimeName((it.type.classifier as KClass<*>).java)
        }
    }${toRuntimeName((func.returnType.classifier as KClass<*>).java)}"


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
        type == Unit::class.java -> "V"
        else -> "L" + type.name.replace(
            '.',
            '/'
        ) + ";"
    }


//    /**
//     * Determines if the given char can be parsed to a primitive type.
//     *
//     * @param c the given char.
//     * @return if the char can be parsed to a primitive.
//     */
//    public fun isPrimitiveType(c: Char): Boolean = when (c) {
//        'V', 'Z', 'C', 'B', 'S', 'I', 'F', 'J', 'D' -> true
//        else -> false
//    }

    /**
     * Determines if the given opcode is a return instruction.
     *
     * @param opcode The opcode to check against.
     * @return If it is a return instruction or not.
     */
    public fun isReturn(opcode: Int): Boolean = opcode.equalsAny(
        IRETURN,
        LRETURN,
        FRETURN,
        DRETURN,
        ARETURN,
        RETURN
    )

    public fun isLoad(opcode: Int): Boolean =
        opcode.equalsAny(ILOAD, LLOAD, FLOAD, DLOAD, ALOAD)

    public fun isStore(opcode: Int) : Boolean =
        opcode.equalsAny(ISTORE, LSTORE, FSTORE, DSTORE, ASTORE)

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
    public fun overloads(first: String, second: String): Boolean =
        Method(first).overloads(Method(second))
//
    public fun opcodeToString(opcode: Int): String? {
        return Printer.OPCODES.getOrNull(opcode)
    }

    // Turn a InsnList into a list of strings
    public fun textifyInsn(insn: InsnList): List<String> {
        return insn.associateWith {
            when (it) {
                is FieldInsnNode -> "${it.owner} ${it.name} ${it.desc}"
                is IincInsnNode -> "${it.`var`} ${it.incr}"
                is IntInsnNode -> "${it.operand}"
                is InvokeDynamicInsnNode -> "${it.name} ${it.desc}"
                is JumpInsnNode -> "${it.label.label}"
                is LdcInsnNode -> "${it.cst}"
                is LineNumberNode -> "${it.line} ${it.start}"
                is LookupSwitchInsnNode -> "${it.dflt.label} ${it.keys} ${it.labels}"
                is MethodInsnNode -> "${it.owner} ${it.name} ${it.desc} ${it.itf}"
                is MultiANewArrayInsnNode -> it.desc
                is TableSwitchInsnNode -> "${it.min} ${it.max} ${it.dflt.label} ${it.labels}"
                is TypeInsnNode -> it.desc
                is VarInsnNode -> "${it.`var`}"
                is LabelNode -> "LABEL ${it.label}"
                else -> ""
            }
        }.map { "${opcodeToString(it.key.opcode)?.let { s -> "$s " } ?: ""}${it.value}" }
    }

}