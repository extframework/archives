package net.yakclient.mixin.internal.instruction;

import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Instruction {
    private final Consumer<MethodVisitor>[] actions;

    @SuppressWarnings("unchecked")
    public Instruction(InstructionBuilder builder) {
        this.actions = builder.actions.toArray(new Consumer[0]);
    }

    public void execute(MethodVisitor visitor) {
        for (Consumer<MethodVisitor> action : actions) {
            action.accept(visitor);
        }
    }

    public static class InstructionBuilder {
        private final List<Consumer<MethodVisitor>> actions;

        public InstructionBuilder() {
            this.actions = new ArrayList<>();
        }

        public InstructionBuilder addInstruction(Consumer<MethodVisitor> visitor) {
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
