package net.yakclient.mixin.internal.methodadapter;

import net.yakclient.mixin.api.InjectionType;
import net.yakclient.mixin.internal.instruction.Instruction;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Queue;

public abstract class MethodInjectionPatternMatcher extends MethodVisitor {
    @FunctionalInterface
    private interface MatcherInitializer {
        MethodInjectionPatternMatcher apply(MethodVisitor v, Queue<Instruction> insn);
    }

    public enum MatcherPattern {
        AFTER_BEGIN(AfterBeginPatternMatcher::new),
        BEFORE_END(BeforeEndPatternMatcher::new),
        BEFORE_INVOKE(BeforeInvokePatternMatcher::new),
        BEFORE_RETURN(BeforeReturnPatternMatcher::new);

        private final MatcherInitializer matcher;

        MatcherPattern(MatcherInitializer matcher) {
            this.matcher = matcher;
        }

        public MethodInjectionPatternMatcher match(MethodVisitor v, Queue<Instruction> insn) {
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

    private final Queue<Instruction> instructions;
    static final int NOT_MATCHED = 0;

    int state = NOT_MATCHED;

    public MethodInjectionPatternMatcher(MethodVisitor visitor, Queue<Instruction> instructions) {
        super(Opcodes.ASM6, visitor);
        this.instructions = instructions;
    }

    void executeInsn() {
        for (Instruction insn : this.instructions) {
            insn.execute(this.mv);
        }
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        super.visitMaxs(maxStack, maxLocals);
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
