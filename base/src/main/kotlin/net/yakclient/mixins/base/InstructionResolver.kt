package net.yakclient.mixins.base

import org.objectweb.asm.tree.InsnList

fun interface InstructionResolver {
    fun get() : InsnList
}

abstract class InstructionReader(
    protected val parentClass: String,
    protected val methodSignature: String
) : InstructionResolver

abstract class InstructionAdapter(
    parent: InstructionResolver
) : InstructionResolver by parent

