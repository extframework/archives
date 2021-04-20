package net.yakclient.mixin.base.internal.methodadapter.core;

import net.yakclient.mixin.base.internal.instruction.Instruction;
import org.objectweb.asm.MethodVisitor;

public class CoreABPatternMatcher extends CoreMixinPatternMatcher {
    public CoreABPatternMatcher(MethodVisitor visitor, Instruction instructions) {
        super(visitor, instructions);
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
