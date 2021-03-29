package net.yakclient.asm;

import net.yakclient.mixin.internal.instruction.Instruction;
import net.yakclient.mixin.internal.instruction.InstructionInterceptor;

public class InstructionPrinter {
    public static void printInsn(Instruction instruction) {
        InstructionInterceptor interceptor = new InstructionInterceptor(null);
        instruction.execute(interceptor);
    }
}