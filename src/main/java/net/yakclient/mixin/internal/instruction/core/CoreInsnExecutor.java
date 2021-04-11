package net.yakclient.mixin.internal.instruction.core;

import net.yakclient.mixin.internal.instruction.Instruction;
import net.yakclient.mixin.internal.instruction.InstructionExecutor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;

public class CoreInsnExecutor implements InstructionExecutor {
    private final Instruction instruction;
    private final MethodVisitor visitor;

    public CoreInsnExecutor(Instruction instruction, MethodVisitor visitor) {
        this.instruction = instruction;
        this.visitor = visitor;
    }

    @Override
    public void execute() {
        for (AbstractInsnNode insn : instruction.getInstructions()) {
            insn.accept(visitor);
        }
    }

}
