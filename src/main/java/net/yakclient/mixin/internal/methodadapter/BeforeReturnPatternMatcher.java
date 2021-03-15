package net.yakclient.mixin.internal.methodadapter;

import net.yakclient.mixin.internal.bytecode.BytecodeMethodModifier;
import net.yakclient.mixin.internal.instruction.Instruction;
import org.objectweb.asm.MethodVisitor;

import java.util.Queue;

public class BeforeReturnPatternMatcher extends MethodInjectionPatternMatcher {
    public BeforeReturnPatternMatcher(MethodVisitor visitor, Queue<BytecodeMethodModifier.PriorityInstruction> instructions, PatternFlag<?>... flags) {
        super(visitor, instructions, flags);
    }

    @Override
    public void visitInsn(int opcode) {
        if (isReturn(opcode)) this.executeInsn();
        super.visitInsn(opcode);
    }

    @Override
    public void visitInsn() {
    }
}
