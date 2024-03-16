package net.yakclient.archives.extension

import org.objectweb.asm.Type

public val Type.isPrimitive: Boolean
    get() = when (sort) {
        Type.VOID,
        Type.BOOLEAN,
        Type.CHAR,
        Type.BYTE,
        Type.SHORT,
        Type.INT,
        Type.FLOAT,
        Type.LONG,
        Type.DOUBLE -> true

        else -> false
    }