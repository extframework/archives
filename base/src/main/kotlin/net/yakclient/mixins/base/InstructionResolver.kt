package net.yakclient.mixins.base

import org.objectweb.asm.tree.InsnList

public fun interface InstructionResolver {
    public fun get() : InsnList
}

public fun interface InstructionReader : InstructionResolver

public class ProvidedInstructionReader(
     private val insn: InsnList
) : InstructionReader {
    override fun get(): InsnList = insn
}

public abstract class DirectInstructionReader(
    protected val parentClass: String,
    protected val methodSignature: String
) : InstructionReader

public abstract class InstructionAdapter(
    parent: InstructionResolver
) : InstructionResolver by parent

