package net.yakclient.mixin.internal.instruction;

import org.objectweb.asm.*;

public class MethodInstructionForwarder extends MethodVisitor {
    protected final Instruction.InstructionBuilder builder;

    public MethodInstructionForwarder(MethodVisitor visitor) {
        super(Opcodes.ASM6, visitor);
        this.builder = new Instruction.InstructionBuilder();
    }

    public Instruction toInstructions() {
        return this.builder.build();
    }

    @Override
    public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
        this.builder.addInstruction(v -> v.visitFrame(type, nLocal, local, nStack, stack));
        super.visitFrame(type, nLocal, local, nStack, stack);
    }

    @Override
    public void visitInsn(int opcode) {
        this.builder.addInstruction(v -> v.visitInsn(opcode));
        super.visitInsn(opcode);
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        this.builder.addInstruction(v -> v.visitIntInsn(opcode, operand));
        super.visitIntInsn(opcode, operand);
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        this.builder.addInstruction(v -> v.visitVarInsn(opcode, var));
        super.visitVarInsn(opcode, var);
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        this.builder.addInstruction(v -> v.visitTypeInsn(opcode, type));
        super.visitTypeInsn(opcode, type);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        this.builder.addInstruction(v -> v.visitFieldInsn(opcode, owner, name, desc));
        super.visitFieldInsn(opcode, owner, name, desc);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        this.builder.addInstruction(v -> v.visitMethodInsn(opcode, owner, name, desc));
        super.visitMethodInsn(opcode, owner, name, desc);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        this.builder.addInstruction(v -> v.visitMethodInsn(opcode, owner, name, desc, itf));
        super.visitMethodInsn(opcode, owner, name, desc, itf);
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
        this.builder.addInstruction(v -> v.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs));
        super.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        this.builder.addInstruction(v -> v.visitJumpInsn(opcode, label));
        super.visitJumpInsn(opcode, label);
    }

    @Override
    public void visitLabel(Label label) {
        this.builder.addInstruction(v -> v.visitLabel(label));
        super.visitLabel(label);
    }

    @Override
    public void visitLdcInsn(Object cst) {
        this.builder.addInstruction(v -> v.visitLdcInsn(cst));
        super.visitLdcInsn(cst);
    }

    @Override
    public void visitIincInsn(int var, int increment) {
        this.builder.addInstruction(v -> v.visitIincInsn(var, increment));
        super.visitIincInsn(var, increment);
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        this.builder.addInstruction(v -> v.visitTableSwitchInsn(min, max, dflt, labels));
        super.visitTableSwitchInsn(min, max, dflt, labels);
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        this.builder.addInstruction(v -> v.visitLookupSwitchInsn(dflt, keys, labels));
        super.visitLookupSwitchInsn(dflt, keys, labels);
    }

    @Override
    public void visitMultiANewArrayInsn(String desc, int dims) {
        this.builder.addInstruction(v -> v.visitMultiANewArrayInsn(desc, dims));
        super.visitMultiANewArrayInsn(desc, dims);
    }

    @Override
    public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
        this.builder.addInstruction(v -> v.visitInsnAnnotation(typeRef, typePath, desc, visible));
        return super.visitInsnAnnotation(typeRef, typePath, desc, visible);
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        this.builder.addInstruction(v -> v.visitTryCatchBlock(start, end, handler, type));
        super.visitTryCatchBlock(start, end, handler, type);
    }

    @Override
    public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
        this.builder.addInstruction(v -> v.visitTryCatchAnnotation(typeRef, typePath, desc, visible));
        return super.visitTryCatchAnnotation(typeRef, typePath, desc, visible);
    }

    @Override
    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        this.builder.addInstruction(v -> v.visitLocalVariable(name, desc, signature, start, end, index));
        super.visitLocalVariable(name, desc, signature, start, end, index);
    }

    @Override
    public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String desc, boolean visible) {
        this.builder.addInstruction(v -> v.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, desc, visible));
        return super.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, desc, visible);
    }

    @Override
    public void visitLineNumber(int line, Label start) {
        this.builder.addInstruction(v -> v.visitLineNumber(line, start));
        super.visitLineNumber(line, start);
    }
}
