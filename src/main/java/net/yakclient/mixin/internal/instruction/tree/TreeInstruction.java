package net.yakclient.mixin.internal.instruction.tree;

import net.yakclient.mixin.internal.instruction.Instruction;
import org.objectweb.asm.tree.InsnList;

public class TreeInstruction implements Instruction {
    private final InsnList instructions;

    public TreeInstruction(InsnList instructions) {
        this.instructions = instructions;
    }

    InsnList getInstructions() {
        return instructions;
    }
}
