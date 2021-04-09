package net.yakclient.mixin.internal.bytecode;

import net.yakclient.mixin.internal.instruction.Instruction;
import net.yakclient.mixin.internal.instruction.core.CoreInstruction;
import org.jetbrains.annotations.NotNull;

public class PriorityInstruction<T extends Instruction> implements Comparable<PriorityInstruction<?>> {
    private final int priority;
    private final T insn;

    public PriorityInstruction(int priority, T insn) {
        this.priority = priority;
        this.insn = insn;
    }

    @Override
    public int compareTo(@NotNull PriorityInstruction o) {
        return o.priority - this.priority;
    }

    public T getInsn() {
        return this.insn;
    }
}
