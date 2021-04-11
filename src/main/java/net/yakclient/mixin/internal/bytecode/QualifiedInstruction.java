package net.yakclient.mixin.internal.bytecode;

import net.yakclient.mixin.internal.instruction.Instruction;
import org.jetbrains.annotations.NotNull;

public class QualifiedInstruction implements Comparable<QualifiedInstruction> {
    private final int priority;
    private final int injectionType;
    private final Instruction insn;

    public QualifiedInstruction(int priority, int type, Instruction insn) {
        this.priority = priority;
        this.injectionType = type;
        this.insn = insn;
    }

    @Override
    public int compareTo(@NotNull QualifiedInstruction o) {
        return o.priority - this.priority;
    }

    public Instruction getInsn() {
        return this.insn;
    }

    public int getPriority() {
        return priority;
    }

    public int getInjectionType() {
        return injectionType;
    }
}
