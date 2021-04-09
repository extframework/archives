package net.yakclient.mixin.internal.instruction.core;

import net.yakclient.mixin.internal.instruction.InstructionExecutor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

public class CoreInsnExecutor implements InstructionExecutor {
    private final CoreInstruction instruction;

    public CoreInsnExecutor(CoreInstruction instruction) {
        this.instruction = instruction;
    }

    @Override
    public void execute(MethodVisitor visitor) {
        for (ByteCodeConsumer action : this.instruction.getActions()) {
            action.accept(visitor);
        }
    }
}
