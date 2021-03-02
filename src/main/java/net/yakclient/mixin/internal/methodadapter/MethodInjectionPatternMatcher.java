package net.yakclient.mixin.internal.methodadapter;

import net.yakclient.mixin.api.InjectionType;
import net.yakclient.mixin.internal.instruction.Instruction;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public abstract class MethodInjectionPatternMatcher extends MethodVisitor {
    @FunctionalInterface
    private interface MatcherInitializer {
        MethodInjectionPatternMatcher apply(MethodVisitor v, Instruction insn);
    }

    public enum MatcherPattern {
        AFTER_BEGIN(AfterBeginPatternMatcher::new),
        BEFORE_END(BeforeEndPatternMatcher::new),
        BEFORE_INVOKE(BeforeEndPatternMatcher::new),
        BEFORE_RETURN(BeforeReturnPatternMatcher::new);

        private final MatcherInitializer matcher;

        MatcherPattern(MatcherInitializer matcher) {
            this.matcher = matcher;
        }

        public MethodInjectionPatternMatcher match(MethodVisitor v, Instruction insn) {
            return this.matcher.apply(v, insn);
        }

        public static MatcherPattern pattern(InjectionType type) {
            switch (type) {
                case BEFORE_END:
                    return BEFORE_END;
                case BEFORE_INVOKE:
                    return BEFORE_INVOKE;
                case BEFORE_RETURN:
                    return BEFORE_RETURN;
                default:
                    return AFTER_BEGIN;
            }
        }
    }

    private final Instruction instruction;
    static final int NOT_MATCHED = 0;

    int state = NOT_MATCHED;

    public MethodInjectionPatternMatcher(MethodVisitor visitor, Instruction instruction) {
        super(Opcodes.ASM6, visitor);
        this.instruction = instruction;
    }

    void executeInsn() {
        this.instruction.execute(this.mv);
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
