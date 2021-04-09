package net.yakclient.mixin.internal.instruction.core;

import net.yakclient.mixin.internal.instruction.Instruction;

public class CoreInstruction implements Instruction {
    private final ByteCodeConsumer[] actions;

    public CoreInstruction(ByteCodeConsumer[] actions) {
        this.actions = actions;
    }

    ByteCodeConsumer[] getActions() {
        return actions;
    }
}
