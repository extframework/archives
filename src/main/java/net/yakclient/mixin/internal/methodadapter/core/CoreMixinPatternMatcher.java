package net.yakclient.mixin.internal.methodadapter.core;

import net.yakclient.YakMixins;
import net.yakclient.mixin.internal.instruction.core.CoreInsnExecutor;
import net.yakclient.mixin.internal.instruction.core.CoreInstruction;
import net.yakclient.mixin.internal.methodadapter.MixinPatternMatcher;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

public abstract class CoreMixinPatternMatcher extends MethodVisitor implements MixinPatternMatcher {
    static final int NOT_MATCHED = 0;
    private final CoreInstruction instructions;
    int state = NOT_MATCHED;

    public CoreMixinPatternMatcher(MethodVisitor visitor, CoreInstruction instructions) {
        super(YakMixins.ASM_VERSION, visitor);
        this.instructions = instructions;
    }

//    @Override
//    public void transform(InsnList instructions) {
//        for (AbstractInsnNode insn : instructions) {
//            insn.accept(this);
//        }
//    }

    void executeInsn() {
        this.executeInsn(this.instructions);
    }

    private void executeInsn(CoreInstruction insn) {
        if (this.mv instanceof CoreMixinPatternMatcher) ((CoreMixinPatternMatcher) this.mv).executeInsn(insn);
        else new CoreInsnExecutor(insn).execute(this.mv);
    }

//    void executeInsn(Queue<BytecodeMethodModifier.PriorityInstruction> instructions) {
//        if (this.mv instanceof MethodInjectionPatternMatcher)
//            ((MethodInjectionPatternMatcher) this.mv).executeInsn(instructions);
//        else for (BytecodeMethodModifier.PriorityInstruction insn : instructions) {
//            insn.getInsn().execute(this.mv);
////            visitor.print();
//        }
//    }

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

    public abstract void visitInsn();


//    public static CoreMixinPatternMatcher pattern(int type, MethodVisitor v, Queue<PriorityInstruction<CoreInstruction>> insn) {
//        switch (type) {
//            case InjectionType.AFTER_BEGIN:
//                return new AfterBeginPatternMatcher(v, insn);
//            case InjectionType.BEFORE_INVOKE:
//                return new BeforeInvokePatternMatcher(v, insn);
//            case InjectionType.BEFORE_RETURN:
//                return new BeforeReturnPatternMatcher(v, insn);
//            case InjectionType.BEFORE_END:
//                return new BeforeEndPatternMatcher(v, insn);
//            case InjectionType.OVERWRITE:
//                return new OverwritePatternMatcher(v, insn);
//            default:
//                return new OpcodeMatcherPattern(v, insn, type);
//        }
//    }

//    public static boolean isOpcodeMatch(int type) {
//
//    }
//    public enum MatcherPattern {
//        AFTER_BEGIN(AfterBeginPatternMatcher::new),
//        BEFORE_END(BeforeEndPatternMatcher::new),
//        BEFORE_INVOKE(BeforeInvokePatternMatcher::new),
//        BEFORE_RETURN(BeforeReturnPatternMatcher::new),
//        OVERWRITE(OverwritePatternMatcher::new),
//        MATCH_OPCODE(OpcodeMatcherPattern::new);
//
//        private final MatcherInitializer matcher;
//
//        MatcherPattern(MatcherInitializer matcher) {
//            this.matcher = matcher;
//        }
//
//        public static MatcherPattern pattern(int type) {
//            switch (type) {
//                case InjectionType.AFTER_BEGIN:
//                    return AFTER_BEGIN;
//                case InjectionType.BEFORE_INVOKE:
//                    return BEFORE_INVOKE;
//                case InjectionType.BEFORE_RETURN:
//                    return BEFORE_RETURN;
//                case InjectionType.BEFORE_END:
//                    return BEFORE_END;
//                case InjectionType.OVERWRITE:
//                    return OVERWRITE;
//                default:
//                    return MATCH_OPCODE;
//            }
//        }
//
//        public MethodInjectionPatternMatcher match(MethodVisitor v, Queue<BytecodeMethodModifier.PriorityInstruction> insn) {
//            return this.matcher.apply(v, insn);
//        }
//    }

//    @FunctionalInterface
//    private interface MatcherInitializer {
//        MethodInjectionPatternMatcher apply(MethodVisitor v, Queue<BytecodeMethodModifier.PriorityInstruction> insn);
//    }
//
//    public static class PatternFlag<T> {
//        @NotNull
//        private final T flag;
//
//        public PatternFlag(@NotNull T parameter) {
//            this.flag = parameter;
//        }
//
//        @NotNull
//        public T getFlag() {
//            return flag;
//        }
//    }
}
