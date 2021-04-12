package net.yakclient.mixin.internal.instruction;

public class InsnAdapter {
    private final InsnAdapter adapter;

    public InsnAdapter(InsnAdapter adapter) {
        this.adapter = adapter;
    }

    public InsnAdapter() {
        this.adapter = null;
    }

    public Instruction adapt(Instruction instruction) {
        if (this.adapter != null) return adapter.adapt(instruction);
        return instruction;
    }
}
