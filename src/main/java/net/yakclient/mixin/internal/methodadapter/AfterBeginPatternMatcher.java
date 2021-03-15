package net.yakclient.mixin.internal.methodadapter;

import net.yakclient.mixin.internal.bytecode.BytecodeMethodModifier;
import net.yakclient.mixin.internal.instruction.Instruction;
import org.objectweb.asm.MethodVisitor;

import java.util.Queue;

public class AfterBeginPatternMatcher extends MethodInjectionPatternMatcher {
    public AfterBeginPatternMatcher(MethodVisitor visitor, Queue<BytecodeMethodModifier.PriorityInstruction> instructions, PatternFlag<?>... flags) {
        super(visitor, instructions, flags);
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
