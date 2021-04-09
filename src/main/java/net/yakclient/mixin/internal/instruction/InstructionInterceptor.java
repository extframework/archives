package net.yakclient.mixin.internal.instruction;

public interface InstructionInterceptor<T extends Instruction> {
    T intercept();
}
