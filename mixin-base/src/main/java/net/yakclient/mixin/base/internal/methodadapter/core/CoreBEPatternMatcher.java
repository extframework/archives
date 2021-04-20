package net.yakclient.mixin.base.internal.methodadapter.core;

import net.yakclient.mixin.base.internal.bytecode.ByteCodeUtils;
import net.yakclient.mixin.base.internal.instruction.Instruction;
import org.objectweb.asm.*;

public class CoreBEPatternMatcher extends CoreMixinPatternMatcher {
    private static final int FOUND_RETURN = 1;
    private static final int FOUND_LAST_RETURN = 2;

    private int returnType;

    public CoreBEPatternMatcher(MethodVisitor visitor, Instruction instructions) {
        super(visitor, instructions);
        this.returnType = Opcodes.RETURN;
    }

    @Override
    public void visitInsn(int opcode) {
        if (ByteCodeUtils.isReturn(opcode)) {
            this.state = FOUND_RETURN;
            this.returnType = opcode;
            return;
        }
        super.visitInsn(opcode);
    }

    @Override
    /* Has cases that will throw */
    public void visitEnd() {
        //Pointless but is nice for cleanup
        if (this.state == FOUND_RETURN)
            this.state = FOUND_LAST_RETURN;
        this.executeInsn();

        super.visitInsn(this.returnType);

        super.visitMaxs(0, 0); /* Should be calculated by the ClassWriter, otherwise it will throw a verify error */
        super.visitEnd();
    }

    @Override
    public void visitInsn() {
        if (state == FOUND_RETURN) {
            state = NOT_MATCHED;
            this.visitInsn(this.returnType);
        }
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) { /* Unused because we have no need to provide debugging tools */ }

    @Override
    public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
        return null; /* We dont need annotations to none-existent references */
    }

    @Override
    public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) { /* Should be calculated automatically so again we have no need to provide it */ }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) { /* We dont want Max's to be called before we inject */ }
}
