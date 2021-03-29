package net.yakclient.mixin.internal.instruction;

import net.yakclient.mixin.internal.methodadapter.ByteCodeConsumer;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;
import java.util.List;

public class Instruction {
    private final ByteCodeConsumer[] actions;

    public Instruction(InstructionBuilder builder) {
        this.actions = builder.actions.toArray(new ByteCodeConsumer[0]);
    }

    public void execute(MethodVisitor visitor) {
        for (ByteCodeConsumer action : actions) {
            action.accept(visitor);
        }
    }

    public static class InstructionBuilder {
        private final List<ByteCodeConsumer> actions;

        public InstructionBuilder() {
            this.actions = new ArrayList<>();
        }

        public InstructionBuilder addInstruction(ByteCodeConsumer visitor) {
            this.actions.add(visitor);
            return this;
        }

        public InstructionBuilder addAll(InstructionBuilder builder) {
            this.actions.addAll(builder.actions);
            return this;
        }

        public Instruction build() {
            return new Instruction(this);
        }
    }
}
