package net.yakclient.mixin.internal.instruction;

import org.objectweb.asm.tree.InsnList;

public class ASMInstruction implements Instruction {
    private final InsnList instructions;

    public ASMInstruction(InsnList instructions) {

        this.instructions = instructions;
    }

    @Override
    public InsnList getInstructions() {
        return instructions;
    }
}