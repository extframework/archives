package net.yakclient.mixin.internal.methodadapter;

import net.yakclient.mixin.internal.instruction.Instruction;
import org.objectweb.asm.MethodVisitor;

public class AfterBeginPatternMatcher extends MethodInjectionPatternMatcher {
    public AfterBeginPatternMatcher(MethodVisitor visitor, Instruction instruction) {
        super(visitor, instruction);
    }

    @Override
    public void visitInsn() {
    }

    @Override
    public void visitCode() {
        super.visitCode();

        this.executeInsn();
    }
}
