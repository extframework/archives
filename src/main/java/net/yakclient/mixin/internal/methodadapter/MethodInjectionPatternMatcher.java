package net.yakclient.mixin.internal.methodadapter;

import net.yakclient.mixin.api.InjectionType;
import net.yakclient.mixin.internal.bytecode.BytecodeMethodModifier;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Queue;

public abstract class MethodInjectionPatternMatcher extends MethodVisitor {
    @FunctionalInterface
    private interface MatcherInitializer {
        MethodInjectionPatternMatcher apply(MethodVisitor v, Queue<BytecodeMethodModifier.PriorityInstruction> insn, PatternFlag<?>... flags);
    }

    public enum MatcherPattern {
        AFTER_BEGIN(AfterBeginPatternMatcher::new),
        BEFORE_END(BeforeEndPatternMatcher::new),
        BEFORE_INVOKE(BeforeInvokePatternMatcher::new),
        BEFORE_RETURN(BeforeReturnPatternMatcher::new),
        OVERWRITE(OverwritePatternMatcher::new),
        MATCH_OPCODE(OpcodeMatcherPattern::new);

        private final MatcherInitializer matcher;

        MatcherPattern(MatcherInitializer matcher) {
            this.matcher = matcher;
        }

        public MethodInjectionPatternMatcher match(MethodVisitor v, Queue<BytecodeMethodModifier.PriorityInstruction> insn, PatternFlag<?>... flags) {
            return this.matcher.apply(v, insn, flags);
        }

        public static MatcherPattern pattern(int type) {
            switch (type) {
                case InjectionType.AFTER_BEGIN:
                    return AFTER_BEGIN;
                case InjectionType.BEFORE_INVOKE:
                    return BEFORE_INVOKE;
                case InjectionType.BEFORE_RETURN:
                    return BEFORE_RETURN;
                case InjectionType.BEFORE_END:
                    return BEFORE_END;
                case InjectionType.OVERWRITE:
                    return OVERWRITE;
                default:
                    return MATCH_OPCODE;
            }
        }
    }

    public static class PatternFlag<T> {
        @NotNull
        private final T flag;

        public PatternFlag(T parameter) {
            this.flag = parameter;
        }
        @NotNull
        public T getFlag() {
            return flag;
        }
    }

    private final Queue<BytecodeMethodModifier.PriorityInstruction> instructions;
    private final PatternFlag<?>[] flags;

    static final int NOT_MATCHED = 0;
    int state = NOT_MATCHED;

    public MethodInjectionPatternMatcher(MethodVisitor visitor, Queue<BytecodeMethodModifier.PriorityInstruction> instructions, PatternFlag<?>... flags) {
        super(Opcodes.ASM9, visitor);
        this.instructions = instructions;
        this.flags = flags;
    }

    void executeInsn() {
        for (BytecodeMethodModifier.PriorityInstruction insn : this.instructions) {
            insn.getInsn().execute(this.mv);
        }
    }


    @Override
    public void visitInsn(int opcode) {
        this.visitInsn();
        super.visitInsn(opcode);
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        this.visitInsn();
        super.visitIntInsn(opcode, operand);
    }

    /*
        Should not be called by anything but this class
     */
    public abstract void visitInsn();

    public static boolean isReturn(int opcode) {
        switch (opcode) {
            case Opcodes.IRETURN:
            case Opcodes.LRETURN:
            case Opcodes.FRETURN:
            case Opcodes.DRETURN:
            case Opcodes.ARETURN:
            case Opcodes.RETURN:
                return true;
        }
        return false;
    }
}
