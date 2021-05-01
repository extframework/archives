package net.yakclient.mixin.base.internal.instruction.core;

import net.yakclient.mixin.base.internal.instruction.Instruction;
import net.yakclient.mixin.base.internal.instruction.InstructionExecutor;
import org.objectweb.asm.MethodVisitor;

public class CoreInsnExecutor implements InstructionExecutor {
    private final Instruction instruction;
    private final MethodVisitor visitor;

    public CoreInsnExecutor(Instruction instruction, MethodVisitor visitor) {
        this.instruction = instruction;
        this.visitor = visitor;
    }

    @Override
    public void execute() {
        instruction.getInstructions().accept(visitor);
//        for (AbstractInsnNode insn : instruction.getInstructions()) {
//            insn.accept(visitor);
//        }
    }

}
