package net.yakclient.mixin.internal.methodadapter;

import net.yakclient.mixin.internal.instruction.Instruction;
import org.objectweb.asm.MethodVisitor;

public class AfterBeginPatternMatcher extends MethodInjectionPatternMatcher {
    private static final int FOUND_BEGIN = 1;

    public AfterBeginPatternMatcher(MethodVisitor visitor, Instruction instruction) {
        super(visitor, instruction);
    }

    @Override
    public void visitInsn() {
    }

    @Override
    public void visitCode() {
        this.state = FOUND_BEGIN;
        this.executeInsn();
        super.visitCode();
    }
}
