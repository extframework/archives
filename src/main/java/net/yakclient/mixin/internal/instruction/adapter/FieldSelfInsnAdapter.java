package net.yakclient.mixin.internal.instruction.adapter;

import net.yakclient.mixin.internal.instruction.InsnAdapter;
import net.yakclient.mixin.internal.instruction.Instruction;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;

public class FieldSelfInsnAdapter extends InsnAdapter {
    private final String clsTo;
    private final String clsFrom;

    public FieldSelfInsnAdapter(InsnAdapter adapter, String clsTo, String clsFrom) {
        super(adapter);
        this.clsTo = clsTo;
        this.clsFrom = clsFrom;
    }

    public FieldSelfInsnAdapter(String clsTo, String clsFrom) {
        this.clsTo = clsTo;
        this.clsFrom = clsFrom;
    }

    @Override
    public Instruction adapt(Instruction instruction) {
        for (AbstractInsnNode insn : instruction.getInstructions()) {
            if (insn instanceof FieldInsnNode &&
                    insn.getOpcode() != Opcodes.GETSTATIC &&
                    ((FieldInsnNode) insn).owner.equals(this.clsFrom))
                ((FieldInsnNode) insn).owner = this.clsTo;
        }
        return super.adapt(instruction);
    }
}
