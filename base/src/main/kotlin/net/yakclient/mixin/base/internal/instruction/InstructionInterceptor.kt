package net.yakclient.mixin.base.internal.instruction

interface InstructionInterceptor {
    fun intercept(): Instruction
}