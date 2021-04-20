package net.yakclient.mixin.base.internal.instruction.tree;

import net.yakclient.mixin.base.internal.instruction.Instruction;
import net.yakclient.mixin.base.internal.instruction.InstructionExecutor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

public class TreeInsnExecutor implements InstructionExecutor {
    private final InsnList insnList;
    final AbstractInsnNode index;
    final Instruction insn;

    public TreeInsnExecutor(InsnList insnList, AbstractInsnNode index, Instruction insn) {
        this.insnList = insnList;
        this.index = index;
        this.insn = insn;
    }

    @Override
    public void execute() {
        this.insnList.insert(this.index, this.insn.getInstructions());
    }


}
