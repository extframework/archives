package net.yakclient.mixin.internal.instruction;

import org.objectweb.asm.MethodVisitor;

public interface InstructionExecutor {
    void execute(MethodVisitor visitor);
}
