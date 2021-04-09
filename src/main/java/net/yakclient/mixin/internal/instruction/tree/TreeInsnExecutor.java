package net.yakclient.mixin.internal.instruction.tree;

import net.yakclient.mixin.internal.instruction.InstructionExecutor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class TreeInsnExecutor implements InstructionExecutor {
    final TreeInstruction insn;
    final AbstractInsnNode index;

    public TreeInsnExecutor(TreeInstruction insn, AbstractInsnNode index) {
        this.insn = insn;
        this.index = index;
    }

    @Override
    public void execute(MethodVisitor visitor) {
        if (!(visitor instanceof MethodNode)) return;
        final MethodNode node = (MethodNode) visitor;
        node.instructions.insert(this.index, this.insn.getInstructions());
    }
}
