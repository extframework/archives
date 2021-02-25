package net.yakclient.mixin.internal.methodadapter;

import net.yakclient.mixin.internal.instruction.Instruction;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class BeforeEndPatternMatcher extends MethodInjectionPatternMatcher {
    private static final int FOUND_RETURN = 1;
    private static final int FOUND_LAST_RETURN = 2;

    private int returnType = Opcodes.RETURN;

    public BeforeEndPatternMatcher(MethodVisitor visitor, Instruction instruction) {
        super(visitor, instruction);
    }

    // int IRETURN = 172; // visitInsn
    //    int LRETURN = 173; // -
    //    int FRETURN = 174; // -
    //    int DRETURN = 175; // -
    //    int ARETURN = 176; // -
    //    int RETURN = 177; // -
    @Override
    public void visitInsn(int opcode) {
        if (this.isReturn(opcode)) {
            this.state = FOUND_RETURN;
            this.returnType = opcode;
            return;
        }
        super.visitInsn(opcode);
    }

    @Override
    public void visitEnd() {
        if (this.state == FOUND_RETURN) {
            this.state = FOUND_LAST_RETURN;
            this.executeInsn();
            super.visitInsn(this.returnType);
        }
        super.visitEnd();
    }

    @Override
    public void visitInsn() {
        if (state == FOUND_RETURN) state = NOT_MATCHED;

    }
}
