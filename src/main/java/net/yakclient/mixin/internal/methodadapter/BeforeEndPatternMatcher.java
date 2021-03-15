package net.yakclient.mixin.internal.methodadapter;

import net.yakclient.mixin.internal.bytecode.BytecodeMethodModifier;
import net.yakclient.mixin.internal.instruction.Instruction;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Queue;

public class BeforeEndPatternMatcher extends MethodInjectionPatternMatcher {
    private static final int FOUND_RETURN = 1;
    private static final int FOUND_LAST_RETURN = 2;

    private int returnType = Opcodes.RETURN;

    public BeforeEndPatternMatcher(MethodVisitor visitor, Queue<BytecodeMethodModifier.PriorityInstruction> instructions, PatternFlag<?>... flags) {
        super(visitor, instructions, flags);
    }

    @Override
    public void visitInsn(int opcode) {
        if (isReturn(opcode)) {
            this.state = FOUND_RETURN;
            this.returnType = opcode;
            return;
        }
        super.visitInsn(opcode);
    }

    @Override
    public void visitEnd() {
        //Pointless but is nice for cleanup
        if (this.state == FOUND_RETURN)
            this.state = FOUND_LAST_RETURN;

        this.executeInsn();
        super.visitInsn(this.returnType);
        //Should be calculated by the ClassWriter, otherwise it will throw a verify error
        super.visitMaxs(0,0);
        super.visitEnd();
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) { /* ... */}

    @Override
    public void visitInsn() {
        if (state == FOUND_RETURN) {
            state = NOT_MATCHED;
            this.visitInsn(this.returnType);
        }
    }
}
