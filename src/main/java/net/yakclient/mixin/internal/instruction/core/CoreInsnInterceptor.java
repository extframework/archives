package net.yakclient.mixin.internal.instruction.core;

import net.yakclient.YakMixins;
import net.yakclient.mixin.internal.instruction.InstructionInterceptor;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.*;

import java.util.ArrayList;
import java.util.List;

public class CoreInsnInterceptor extends MethodVisitor implements InstructionInterceptor<CoreInstruction> {
    private static final int NOTHING_FOUND = 0;
    private static final int RETURN_FOUND = 1;
    private static final int FINAL_RETURN_FOUND = 2;

    protected final List<ByteCodeConsumer> insn;
    @NotNull
    private final String ownerSource;
    @NotNull
    private final String ownerDest;

    private int state;

    public CoreInsnInterceptor(@NotNull String ownerSource, @NotNull String ownerDest) {
        super(YakMixins.ASM_VERSION);
        this.insn = new ArrayList<>();
        this.ownerSource = ownerSource.replace('.', '/');
        this.ownerDest = ownerDest.replace('.', '/');

        this.state = NOTHING_FOUND;
    }

    @Override
    public CoreInstruction intercept() {
        return new CoreInstruction(insn.toArray(new ByteCodeConsumer[0]));
    }

    @Override
    public void visitCode() {
        this.insn.clear();
        super.visitCode();
    }

    //Internal
    private void visitInsn() {
        if (this.state == RETURN_FOUND) {
            this.state = NOTHING_FOUND;
            this.visitInsn(Opcodes.RETURN);
        }
    }

    @Override
    public void visitInsn(int opcode) {
        if (opcode == Opcodes.RETURN) this.state = RETURN_FOUND;
        else {
            visitInsn();
            this.insn.add(v -> v.visitInsn(opcode));
        }
        super.visitInsn(opcode);
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        this.visitInsn();
        this.insn.add(v -> v.visitVarInsn(opcode, var));
        super.visitVarInsn(opcode, var);
    }

    @Override
    public void visitEnd() {
        if (this.state == RETURN_FOUND) {
            this.state = FINAL_RETURN_FOUND;
        }
        super.visitEnd();
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        this.insn.add(v -> v.visitIntInsn(opcode, operand));
        super.visitIntInsn(opcode, operand);
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        this.insn.add(v -> v.visitTypeInsn(opcode, type));
        super.visitTypeInsn(opcode, type);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        if (this.ownerSource.equals(owner) && opcode != Opcodes.GETSTATIC && opcode != Opcodes.PUTSTATIC)
            this.insn.add(v -> v.visitFieldInsn(opcode, this.ownerDest, name, desc));
        else this.insn.add(v -> v.visitFieldInsn(opcode, owner, name, desc));
        super.visitFieldInsn(opcode, owner, name, desc);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        if (this.ownerSource.equals(owner) && opcode != Opcodes.INVOKESTATIC)
            this.insn.add(v -> v.visitMethodInsn(opcode, this.ownerDest, name, desc));
        else this.insn.add(v -> v.visitMethodInsn(opcode, owner, name, desc));
        super.visitMethodInsn(opcode, owner, name, desc);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        if (this.ownerSource.equals(owner) && opcode != Opcodes.INVOKESTATIC)
            this.insn.add(v -> v.visitMethodInsn(opcode, this.ownerDest, name, desc, itf));
        else this.insn.add(v -> v.visitMethodInsn(opcode, owner, name, desc, itf));
        super.visitMethodInsn(opcode, owner, name, desc, itf);
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
        this.insn.add(v -> v.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs));
        super.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        this.insn.add(v -> v.visitJumpInsn(opcode, label));
        super.visitJumpInsn(opcode, label);
    }

    @Override
    public void visitLabel(Label label) {
        this.insn.add(v -> v.visitLabel(label));
        super.visitLabel(label);
    }

    @Override
    public void visitLdcInsn(Object cst) {
        this.insn.add(v -> v.visitLdcInsn(cst));
        super.visitLdcInsn(cst);
    }

    @Override
    public void visitIincInsn(int var, int increment) {
        this.insn.add(v -> v.visitIincInsn(var, increment));
        super.visitIincInsn(var, increment);
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        this.insn.add(v -> v.visitTableSwitchInsn(min, max, dflt, labels));
        super.visitTableSwitchInsn(min, max, dflt, labels);
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        this.insn.add(v -> v.visitLookupSwitchInsn(dflt, keys, labels));
        super.visitLookupSwitchInsn(dflt, keys, labels);
    }

    @Override
    public void visitMultiANewArrayInsn(String desc, int dims) {
        this.insn.add(v -> v.visitMultiANewArrayInsn(desc, dims));
        super.visitMultiANewArrayInsn(desc, dims);
    }

    @Override
    public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
        this.insn.add(v -> v.visitInsnAnnotation(typeRef, typePath, desc, visible));
        return super.visitInsnAnnotation(typeRef, typePath, desc, visible);
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        this.insn.add(v -> v.visitTryCatchBlock(start, end, handler, type));
        super.visitTryCatchBlock(start, end, handler, type);
    }

    @Override
    public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
        this.insn.add(v -> v.visitTryCatchAnnotation(typeRef, typePath, desc, visible));
        return super.visitTryCatchAnnotation(typeRef, typePath, desc, visible);
    }

    @Override
    public void visitLineNumber(int line, Label start) {
        this.insn.add(v -> v.visitLineNumber(line, start));
        super.visitLineNumber(line, start);
    }


}
