package net.yakclient.mixins.base

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.InsnList
import java.io.DataInputStream
import java.io.IOException
import java.lang.reflect.Method
import java.util.function.Consumer

object ByteCodeUtils {
    const val ASM_VERSION = Opcodes.ASM9


    fun removeReturnType(type: String?): String {
        return type!!.substring(0, type.indexOf(')') + 1)
    }

    fun jvmName(method: Method): String = "${method.name}${
        method.parameterTypes.joinToString(
            prefix = "(",
            postfix = ")"
        ) { primitiveType(it).toString() }
    }"

    fun descriptorsSame(method1: String?, method2: String?): Boolean {
        return removeReturnType(method1) == removeReturnType(method2)
    }

    fun primitiveType(cls: Class<*>): Char? = when (cls) {
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


    fun primitiveType(c: Char): Class<*>? = when (c) {
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


    fun toRuntimeName(type: Class<*>): String = when {
        type.isPrimitive -> primitiveType(type).toString()
        type.isArray -> type.name
        else -> "L" + type.name.replace(
            '.',
            '/'
        ) + ";"
    }


    fun isPrimitiveType(c: Char): Boolean = when (c) {
        'V', 'Z', 'C', 'B', 'S', 'I', 'F', 'J', 'D' -> true
        else -> false
    }


    fun classExists(clazz: String): Boolean {
        return try {
            loadClassBytes(clazz) != null
        } catch (e: IOException) {
            false
        } catch (e: ClassNotFoundException) {
            false
        }
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    fun loadClassBytes(name: String): ByteArray? {
        ClassLoader.getSystemResourceAsStream(name.replace('.', '/') + ".class").use { cIn ->
            if (cIn == null) return null
            DataInputStream(cIn).use { `in` ->
                val buf = ByteArray(cIn.available())
                `in`.readFully(buf)
                return buf
            }
        }
    }

    /**
     * This is purely meant for debugging/testing purposes, it will be slow
     * and has no use during production runtime.
     *
     * @param opcode The opcode to translate.
     * @return The name of the opcode.
     * @throws IllegalArgumentException If the opcode is not found.
     */
    @Throws(IllegalArgumentException::class)
    fun opcodeToString(opcode: Int): String = when (opcode) {
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


    fun isReturn(opcode: Int): Boolean {
        when (opcode) {
            Opcodes.IRETURN, Opcodes.LRETURN, Opcodes.FRETURN, Opcodes.DRETURN, Opcodes.ARETURN, Opcodes.RETURN -> return true
        }
        return false
    }

    fun isLocalsStore(opcode: Int): Boolean {
        when (opcode) {
            Opcodes.ASTORE, Opcodes.DSTORE, Opcodes.FSTORE, Opcodes.ISTORE, Opcodes.LSTORE -> return true
        }
        return false
    }

    fun toNodes(instruction: InsnList): List<AbstractInsnNode> {
        val nodes = ArrayList<AbstractInsnNode>()
        instruction.forEach(Consumer { e: AbstractInsnNode -> nodes.add(e) })
        return nodes
    }

    fun getRuntimeName(cls: Class<*>): String {
        return cls.name.replace("\\.".toRegex(), "/")
    }

    fun sameSignature(first: String, second: String): Boolean = MethodSignature.of(first)
        .matches(MethodSignature.of(second))

    data class MethodSignature(
        val name: String,
        val desc: String,
        val returnType: String
    ) {
        fun matches(other: MethodSignature) : Boolean = other.name == name && other.desc == desc

        companion object {
            const val NON_ARRAY_PATTERN = "[ZCBSIFJD]|(?:L.+;)"

            const val ANY_VALUE_PATTERN = "$NON_ARRAY_PATTERN|(?:\\[+(?:$NON_ARRAY_PATTERN))"

            const val SIGNATURE_PATTERN = "^(.+)\\(((?:$ANY_VALUE_PATTERN)*)\\)($ANY_VALUE_PATTERN|V)?$"

            fun of(signature: String): MethodSignature {
                val regex = Regex(SIGNATURE_PATTERN)
                check(regex.matches(signature)) { "Invalid method signature: $signature" }

                return regex.find(signature)!!.groupValues.let { MethodSignature(it[1], it[2], if (it.size == 4) it[3] else "UNKNOWN") }
            }
        }
    }

    fun byteCodeSignature(method: Method, name: String = method.name): String {
        val methodReturn = method.returnType
        val builder = StringBuilder(name)
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