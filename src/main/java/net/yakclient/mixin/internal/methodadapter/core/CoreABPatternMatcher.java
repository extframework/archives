package net.yakclient.mixin.internal.methodadapter.core;

import net.yakclient.mixin.internal.bytecode.PriorityInstruction;
import net.yakclient.mixin.internal.instruction.core.CoreInstruction;
import org.objectweb.asm.MethodVisitor;

import java.util.Queue;

public class CoreABPatternMatcher extends CoreMixinPatternMatcher {
    public CoreABPatternMatcher(MethodVisitor visitor, CoreInstruction instructions) {
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
