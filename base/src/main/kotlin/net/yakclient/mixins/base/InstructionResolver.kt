package net.yakclient.mixins.base

import org.objectweb.asm.tree.InsnList

fun interface InstructionResolver {
    fun get() : InsnList
}

fun interface InstructionReader : InstructionResolver

class ProvidedInstructionReader(
     private val insn: InsnList
) : InstructionReader {
    override fun get(): InsnList = insn
}

abstract class DirectInstructionReader(
    protected val parentClass: String,
    protected val methodSignature: String
) : InstructionReader

abstract class InstructionAdapter(
    parent: InstructionResolver
) : InstructionResolver by parent

