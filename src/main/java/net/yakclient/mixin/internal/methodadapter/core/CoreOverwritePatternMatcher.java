package net.yakclient.mixin.internal.methodadapter.core;

import net.yakclient.mixin.internal.bytecode.PriorityInstruction;
import net.yakclient.mixin.internal.instruction.core.CoreInstruction;
import org.objectweb.asm.*;

import java.util.Queue;

public class CoreOverwritePatternMatcher extends CoreMixinPatternMatcher {
    public CoreOverwritePatternMatcher(MethodVisitor visitor, CoreInstruction instructions) {
        super(visitor, instructions);
    }

    @Override
    public void visitCode() {
        super.visitCode();

        this.executeInsn();
    }

    @Override
    public void visitInsn() {
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor) {
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
    }

    @Override
    public void visitLabel(Label label) {
    }

    @Override
    public void visitLdcInsn(Object value) {
    }

    @Override
    public void visitIincInsn(int var, int increment) {
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
    }

    @Override
    public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
    }

    @Override
    public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return null;
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
    }

    @Override
    public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        return null;
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
    }

    @Override
    public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
        return null;
    }

    @Override
    public void visitLineNumber(int line, Label start) {
    }
}
