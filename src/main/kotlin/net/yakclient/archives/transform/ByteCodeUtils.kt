package net.yakclient.archives.transform

import org.objectweb.asm.Opcodes
import java.io.File
import java.lang.reflect.Method

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

    public fun signatureOf(signature: String) : MethodSignature = MethodSignature.of(signature)
    /**
     * Returns the runtime method signature of the provided method.
     *
     * @param method The method to parse the name of.
     * @return the parsed name.
     */
    public fun byteCodeSignature(method: Method): String {
        File.pathSeparator
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

    public fun opcodeToString(opcode: Int): String {
        return when (opcode) {
            0 -> "NOP"
            1 -> "ACONST_NULL"
            2 -> "ICONST_M1"
            3 -> "ICONST_0"
            4 -> "ICONST_1"
            5 -> "ICONST_2"
            6 -> "ICONST_3"
            7 -> "ICONST_4"
            8 -> "ICONST_5"
            9 -> "LCONST_0"
            10 -> "LCONST_1"
            11 -> "FCONST_0"
            12 -> "FCONST_1"
            13 -> "FCONST_2"
            14 -> "DCONST_0"
            15 -> "DCONST_1"
            16 -> "BIPUSH"
            17 -> "SIPUSH"
            18 -> "LDC"
            21 -> "ILOAD"
            22 -> "LLOAD"
            23 -> "FLOAD"
            24 -> "DLOAD"
            25 -> "ALOAD"
            46 -> "IALOAD"
            47 -> "LALOAD"
            48 -> "FALOAD"
            49 -> "DALOAD"
            50 -> "AALOAD"
            51 -> "AALOAD"
            52 -> "CALOAD"
            53 -> "SALOAD"
            54 -> "ISTORE"
            55 -> "LSTORE"
            56 -> "FSTORE"
            57 -> "DSTORE"
            58 -> "ASTORE"
            79 -> "IASTORE"
            80 -> "LASTORE"
            81 -> "FASTORE"
            82 -> "DASTORE"
            83 -> "AASTORE"
            84 -> "BASTORE"
            85 -> "CASTORE"
            86 -> "SASTORE"
            87 -> "POP"
            88 -> "POP2"
            89 -> "DUP"
            90 -> "DUP_X1"
            91 -> "DUP_X2"
            92 -> "DUP2"
            93 -> "DUP2_X1"
            94 -> "DUP2_X2"
            95 -> "SWAP"
            96 -> "IADD"
            97 -> "LADD"
            98 -> "FADD"
            99 -> "DADD"
            100 -> "ISUB"
            101 -> "LSUB"
            102 -> "FSUB"
            103 -> "DSUB"
            104 -> "IMUL"
            105 -> "LMUL"
            106 -> "FMUL"
            107 -> "DMUL"
            108 -> "IDIV"
            109 -> "LDIV"
            110 -> "FDIV"
            111 -> "DDIV"
            112 -> "IREM"
            113 -> "LREM"
            114 -> "FREM"
            115 -> "DREM"
            116 -> "INEG"
            117 -> "LNEG"
            118 -> "FNEG"
            119 -> "DNEG"
            120 -> "ISHL"
            121 -> "LSHL"
            122 -> "ISHR"
            123 -> "LSHR"
            124 -> "IUSHR"
            125 -> "LUSHR"
            126 -> "IAND"
            127 -> "LAND"
            128 -> "IOR"
            129 -> "LOR"
            130 -> "IXOR"
            131 -> "LXOR"
            132 -> "IINC"
            133 -> "I2L"
            134 -> "I2F"
            135 -> "I2D"
            136 -> "L2I"
            137 -> "L2F"
            138 -> "L2D"
            139 -> "F2I"
            140 -> "F2L"
            141 -> "F2D"
            142 -> "D2I"
            143 -> "D2L"
            144 -> "D2F"
            145 -> "I2B"
            146 -> "I2C"
            147 -> "I2S"
            148 -> "LCMP"
            149 -> "FCMPL"
            150 -> "FCMPG"
            151 -> "DCMPL"
            152 -> "DCMPG"
            153 -> "IFEQ"
            154 -> "IFNE"
            155 -> "IFLT"
            156 -> "IFGE"
            157 -> "IFGT"
            158 -> "IFLE"
            159 -> "IF_ICMPEQ"
            160 -> "IF_ICMPNE"
            161 -> "IF_ICMPLT"
            162 -> "IF_ICMPGE"
            163 -> "IF_ICMPGT"
            164 -> "IF_ICMPLE"
            165 -> "IF_ACMPEQ"
            166 -> "IF_ACMPNE"
            167 -> "GOTO"
            168 -> "JSR"
            169 -> "RET"
            170 -> "TABLESWITCH"
            171 -> "LOOKUPSWITCH"
            172 -> "IRETURN"
            173 -> "LRETURN"
            174 -> "FRETURN"
            175 -> "DRETURN"
            176 -> "ARETURN"
            177 -> "RETURN"
            178 -> "GETSTATIC "
            179 -> "PUTSTATIC"
            180 -> "GETFIELD"
            181 -> "PUTFIELD"
            182 -> "INVOKEVIRTUAL "
            183 -> "INVOKESPECIAL"
            184 -> "INVOKESTATIC"
            185 -> "INVOKEINTERFACE"
            186 -> "INVOKEDYNAMIC"
            187 -> "NEW"
            188 -> "NEWARRAY"
            189 -> "ANEWARRAY"
            190 -> "ARRAYLENGTH"
            191 -> "ATHROW"
            192 -> "CHECKCAST"
            193 -> "INSTANCEOF"
            194 -> "MONITORENTER"
            195 -> "MONITOREXIT"
            197 -> "MULTIANEWARRAY"
            198 -> "IFNULL"
            199 -> "IFNONNULL"
            else -> throw IllegalArgumentException("Invalid opcode")
        }
    }
}