package net.yakclient.mixins.base.internal.instruction

interface InstructionInterceptor {
    fun intercept(): Instruction
}