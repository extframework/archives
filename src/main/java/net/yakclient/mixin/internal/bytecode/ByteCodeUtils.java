package net.yakclient.mixin.internal.bytecode;

public class ByteCodeUtils {
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
}
