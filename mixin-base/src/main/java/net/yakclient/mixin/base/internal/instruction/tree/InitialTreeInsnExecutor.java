package net.yakclient.mixin.base.internal.instruction.tree;

import net.yakclient.mixin.base.internal.instruction.Instruction;
import net.yakclient.mixin.base.internal.instruction.InstructionExecutor;
import org.objectweb.asm.tree.InsnList;

public class InitialTreeInsnExecutor implements InstructionExecutor {
    private final InsnList insnList;
    final Instruction insn;

    public InitialTreeInsnExecutor(InsnList insnList, Instruction insn) {
        this.insnList = insnList;
        this.insn = insn;
    }

    @Override
    public void execute() {
        this.insnList.insert(this.insn.getInstructions());
    }
}
