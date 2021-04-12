package net.yakclient.mixin.internal.instruction.adapter;

import net.yakclient.mixin.internal.instruction.InsnAdapter;
import net.yakclient.mixin.internal.instruction.Instruction;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

public class MethodSelfInsnAdapter extends InsnAdapter {
    private final String clsTo;
    private final String clsFrom;

    public MethodSelfInsnAdapter(InsnAdapter adapter, String clsTo, String clsFrom) {
        super(adapter);
        this.clsTo = clsTo;
        this.clsFrom = clsFrom;
    }

    public MethodSelfInsnAdapter(String clsTo, String clsFrom) {
        this.clsTo = clsTo;
        this.clsFrom = clsFrom;
    }

    @Override
    public Instruction adapt(Instruction instruction) {
        for (AbstractInsnNode insn : instruction.getInstructions()) {
            if (insn instanceof MethodInsnNode &&
                    insn.getOpcode() != Opcodes.INVOKESTATIC &&
                    ((MethodInsnNode) insn).owner.equals(this.clsFrom))
                ((MethodInsnNode) insn).owner = this.clsTo;
        }
        return super.adapt(instruction);
    }
}
