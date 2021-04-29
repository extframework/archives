package net.yakclient.mixin.base.internal.instruction;

import org.objectweb.asm.tree.InsnList;

public class DirectInstruction implements Instruction {
    private final InsnList instructions;

    public DirectInstruction(InsnList instructions) {

        this.instructions = instructions;
    }

    @Override
    public InsnList getInstructions() {
        return instructions;
    }
}