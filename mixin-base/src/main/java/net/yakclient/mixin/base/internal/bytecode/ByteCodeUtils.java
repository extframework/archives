package net.yakclient.mixin.base.internal.bytecode;

import net.yakclient.mixin.mixin.base.internal.ASMType;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;

import java.io.DataInputStream;
import java.io.IOException;

public class ByteCodeUtils {
    public static final ASMType DEFAULT_ASM_MODE = ASMType.TREE;
    public static final int ASM_VERSION = Opcodes.ASM9;


    public static String removeReturnType(String type) {
        return type.substring(0, type.indexOf(')') + 1);
    }

    public static boolean descriptorsSame(String method1, String method2) {
        return removeReturnType(method1).equals(removeReturnType(method2));
    }

    public static char primitiveType(Class<?> cls) {
        if (cls == void.class) return 'V';
        if (cls == boolean.class) return 'Z';
        if (cls == char.class) return 'C';
        if (cls == byte.class) return 'B';
        if (cls == short.class) return 'S';
        if (cls == int.class) return 'I';
        if (cls == float.class) return 'F';
        if (cls == long.class) return 'J';
        if (cls == double.class) return 'D';
        throw new IllegalArgumentException("Class must be a primitive!");
    }

    public static Class<?> primitiveType(char c) {
        if (c == 'V') return void.class;
        if (c == 'Z') return boolean.class;
        if (c == 'C') return char.class;
        if (c == 'B') return boolean.class;
        if (c == 'S') return short.class;
        if (c == 'I') return int.class;
        if (c == 'F') return float.class;
        if (c == 'J') return long.class;
        if (c == 'D') return double.class;
        throw new IllegalArgumentException("Class must be a primitive!");
    }

    public static boolean isPrimitiveType(char c) {
        switch (c) {
            case 'V':
            case 'Z':
            case 'C':
            case 'B':
            case 'S':
            case 'I':
            case 'F':
            case 'J':
            case 'D':
                return true;
        }
        return false;
    }

    public static boolean classExists(String clazz) {
        try {
            return loadClassBytes(clazz) != null;
        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
    }

    @Nullable
    public static byte[] loadClassBytes(String name) throws IOException, ClassNotFoundException {
        try (var cIn = ClassLoader.getSystemResourceAsStream(name.replace('.', '/') + ".class")) {
            if (cIn == null) return null;
            try (var in = new DataInputStream(cIn)) {
                final var buf = new byte[cIn.available()];

                in.readFully(buf);
                return buf;
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
    public static String opcodeToString(int opcode) throws IllegalArgumentException {
        switch (opcode) {
            case 0:
                return "NOP";
            case 1:
                return "ACONST_NULL";
            case 2:
                return "ICONST_M1";
            case 3:
                return "ICONST_0";
            case 4:
                return "ICONST_1";
            case 5:
                return "ICONST_2";
            case 6:
                return "ICONST_3";
            case 7:
                return "ICONST_4";
            case 8:
                return "ICONST_5";
            case 9:
                return "LCONST_0";
            case 10:
                return "LCONST_1";
            case 11:
                return "FCONST_0";
            case 12:
                return "FCONST_1";
            case 13:
                return "FCONST_2";
            case 14:
                return "DCONST_0";
            case 15:
                return "DCONST_1";
            case 16:
                return "BIPUSH";
            case 17:
                return "SIPUSH";
            case 18:
                return "LDC";
            case 21:
                return "ILOAD";
            case 22:
                return "LLOAD";
            case 23:
                return "FLOAD";
            case 24:
                return "DLOAD";
            case 25:
                return "ALOAD";
            case 46:
                return "IALOAD";
            case 47:
                return "LALOAD";
            case 48:
                return "FALOAD";
            case 49:
                return "DALOAD";
            case 50:
                return "AALOAD";
            case 51:
                return "AALOAD";
            case 52:
                return "CALOAD";
            case 53:
                return "SALOAD";
            case 54:
                return "ISTORE";
            case 55:
                return "LSTORE";
            case 56:
                return "FSTORE";
            case 57:
                return "DSTORE";
            case 58:
                return "ASTORE";
            case 79:
                return "IASTORE";
            case 80:
                return "LASTORE";
            case 81:
                return "FASTORE";
            case 82:
                return "DASTORE";
            case 83:
                return "AASTORE";
            case 84:
                return "BASTORE";
            case 85:
                return "CASTORE";
            case 86:
                return "SASTORE";
            case 87:
                return "POP";
            case 88:
                return "POP2";
            case 89:
                return "DUP";
            case 90:
                return "DUP_X1";
            case 91:
                return "DUP_X2";
            case 92:
                return "DUP2";
            case 93:
                return "DUP2_X1";
            case 94:
                return "DUP2_X2";
            case 95:
                return "SWAP";
            case 96:
                return "IADD";
            case 97:
                return "LADD";
            case 98:
                return "FADD";
            case 99:
                return "DADD";
            case 100:
                return "ISUB";
            case 101:
                return "LSUB";
            case 102:
                return "FSUB";
            case 103:
                return "DSUB";
            case 104:
                return "IMUL";
            case 105:
                return "LMUL";
            case 106:
                return "FMUL";
            case 107:
                return "DMUL";
            case 108:
                return "IDIV";
            case 109:
                return "LDIV";
            case 110:
                return "FDIV";
            case 111:
                return "DDIV";
            case 112:
                return "IREM";
            case 113:
                return "LREM";
            case 114:
                return "FREM";
            case 115:
                return "DREM";
            case 116:
                return "INEG";
            case 117:
                return "LNEG";
            case 118:
                return "FNEG";
            case 119:
                return "DNEG";
            case 120:
                return "ISHL";
            case 121:
                return "LSHL";
            case 122:
                return "ISHR";
            case 123:
                return "LSHR";
            case 124:
                return "IUSHR";
            case 125:
                return "LUSHR";
            case 126:
                return "IAND";
            case 127:
                return "LAND";
            case 128:
                return "IOR";
            case 129:
                return "LOR";
            case 130:
                return "IXOR";
            case 131:
                return "LXOR";
            case 132:
                return "IINC";
            case 133:
                return "I2L";
            case 134:
                return "I2F";
            case 135:
                return "I2D";
            case 136:
                return "L2I";
            case 137:
                return "L2F";
            case 138:
                return "L2D";
            case 139:
                return "F2I";
            case 140:
                return "F2L";
            case 141:
                return "F2D";
            case 142:
                return "D2I";
            case 143:
                return "D2L";
            case 144:
                return "D2F";
            case 145:
                return "I2B";
            case 146:
                return "I2C";
            case 147:
                return "I2S";
            case 148:
                return "LCMP";
            case 149:
                return "FCMPL";
            case 150:
                return "FCMPG";
            case 151:
                return "DCMPL";
            case 152:
                return "DCMPG";
            case 153:
                return "IFEQ";
            case 154:
                return "IFNE";
            case 155:
                return "IFLT";
            case 156:
                return "IFGE";
            case 157:
                return "IFGT";
            case 158:
                return "IFLE";
            case 159:
                return "IF_ICMPEQ";
            case 160:
                return "IF_ICMPNE";
            case 161:
                return "IF_ICMPLT";
            case 162:
                return "IF_ICMPGE";
            case 163:
                return "IF_ICMPGT";
            case 164:
                return "IF_ICMPLE";
            case 165:
                return "IF_ACMPEQ";
            case 166:
                return "IF_ACMPNE";
            case 167:
                return "GOTO";
            case 168:
                return "JSR";
            case 169:
                return "RET";
            case 170:
                return "TABLESWITCH";
            case 171:
                return "LOOKUPSWITCH";
            case 172:
                return "IRETURN";
            case 173:
                return "LRETURN";
            case 174:
                return "FRETURN";
            case 175:
                return "DRETURN";
            case 176:
                return "ARETURN";
            case 177:
                return "RETURN";
            case 178:
                return "GETSTATIC ";
            case 179:
                return "PUTSTATIC";
            case 180:
                return "GETFIELD";
            case 181:
                return "PUTFIELD";
            case 182:
                return "INVOKEVIRTUAL ";
            case 183:
                return "INVOKESPECIAL";
            case 184:
                return "INVOKESTATIC";
            case 185:
                return "INVOKEINTERFACE";
            case 186:
                return "INVOKEDYNAMIC";
            case 187:
                return "NEW";
            case 188:
                return "NEWARRAY";
            case 189:
                return "ANEWARRAY";
            case 190:
                return "ARRAYLENGTH";
            case 191:
                return "ATHROW";
            case 192:
                return "CHECKCAST";
            case 193:
                return "INSTANCEOF";
            case 194:
                return "MONITORENTER";
            case 195:
                return "MONITOREXIT";
            case 197:
                return "MULTIANEWARRAY";
            case 198:
                return "IFNULL";
            case 199:
                return "IFNONNULL";
            default:
                throw new IllegalArgumentException("Invalid opcode");
        }
    }

    public static String bytesToHex(byte[] bytes) {
        final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

        final char[] hexChars = new char[bytes.length * 2];
        for (var j = 0; j < bytes.length; j++) {
            final int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static boolean isReturn(int opcode) {
        switch (opcode) {
            case Opcodes.IRETURN:
            case Opcodes.LRETURN:
            case Opcodes.FRETURN:
            case Opcodes.DRETURN:
            case Opcodes.ARETURN:
            case Opcodes.RETURN:
                return true;
        }
        return false;
    }

    public static boolean isLocalsStore(int opcode) {
        switch (opcode) {
            case Opcodes.ASTORE:
            case Opcodes.DSTORE:
            case Opcodes.FSTORE:
            case Opcodes.ISTORE:
            case Opcodes.LSTORE:
                return true;
        }
        return false;
    }
}
