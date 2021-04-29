package net.yakclient.mixin.base.internal.instruction;

import net.yakclient.mixin.base.internal.bytecode.ByteCodeUtils;
import org.jetbrains.annotations.TestOnly;
import org.objectweb.asm.*;

import java.util.ArrayList;
import java.util.List;

@TestOnly
public class InstructionPrinter extends MethodVisitor {
    private final List<String> instructions;

    public InstructionPrinter(MethodVisitor visitor) {
        super(ByteCodeUtils.ASM_VERSION, visitor);
        this.instructions = new ArrayList<String>() {
            @Override
            public boolean add(String o) {
                System.out.println(o);
                return super.add(o);
            }
        };
    }

    @Override
    public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
        this.instructions.add("VISIT FRAME");
        super.visitFrame(type, numLocal, local, numStack, stack);
    }

    @Override
    public void visitInsn(int opcode) {
        this.instructions.add(ByteCodeUtils.opcodeToString(opcode));
        super.visitInsn(opcode);
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        this.instructions.add(ByteCodeUtils.opcodeToString(opcode) + " OPERAND: " + operand);
        super.visitIntInsn(opcode, operand);
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        this.instructions.add(ByteCodeUtils.opcodeToString(opcode) + " VAR: " + var);
        super.visitVarInsn(opcode, var);
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        this.instructions.add(ByteCodeUtils.opcodeToString(opcode) + " TYPE: " + type);
        super.visitTypeInsn(opcode, type);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        this.instructions.add(ByteCodeUtils.opcodeToString(opcode) + " owner: " + owner + " name: " + name + " descriptor: " + descriptor);
        super.visitFieldInsn(opcode, owner, name, descriptor);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor) {
        this.instructions.add(ByteCodeUtils.opcodeToString(opcode) + " owner: " + owner + " name: " + name + " descriptor: " + descriptor);
        super.visitMethodInsn(opcode, owner, name, descriptor);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        this.instructions.add(ByteCodeUtils.opcodeToString(opcode) + " owner: " + owner + " name: " + name + " descriptor: " + descriptor + (isInterface ? "isInterface" : ""));
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
        this.instructions.add("INVOKE_DYNAMIC");
        super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        this.instructions.add("Jump: " + ByteCodeUtils.opcodeToString(opcode) + " Label: " + label);
        super.visitJumpInsn(opcode, label);
    }

    @Override
    public void visitLabel(Label label) {
        this.instructions.add("Label: " + label);
        super.visitLabel(label);
    }

    @Override
    public void visitLdcInsn(Object value) {
        this.instructions.add("LDC: " + value);
        super.visitLdcInsn(value);
    }

    @Override
    public void visitIincInsn(int var, int increment) {
        this.instructions.add("Iinc");
        super.visitIincInsn(var, increment);
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        this.instructions.add("Table switch");
        super.visitTableSwitchInsn(min, max, dflt, labels);
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        this.instructions.add("Lookup switch");
        super.visitLookupSwitchInsn(dflt, keys, labels);
    }

    @Override
    public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
        this.instructions.add("Multi array");
        super.visitMultiANewArrayInsn(descriptor, numDimensions);
    }

    @Override
    public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        this.instructions.add("Annotation");
        return super.visitInsnAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        this.instructions.add("Try catch block");
        super.visitTryCatchBlock(start, end, handler, type);
    }

    @Override
    public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        this.instructions.add("try-catch Annotation");
        return super.visitTryCatchAnnotation(typeRef, typePath, descriptor, visible);
    }

    @Override
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        this.instructions.add("Local Variable name: " + name + " descriptor: " + descriptor + " signature: " + signature);
        super.visitLocalVariable(name, descriptor, signature, start, end, index);
    }

    @Override
    public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
        this.instructions.add("Local variable insn");
        return super.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, descriptor, visible);
    }

    @Override
    public void visitLineNumber(int line, Label start) {
        this.instructions.add("Line: " + line + " Label: " + start);
        super.visitLineNumber(line, start);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        this.instructions.add("Stack: " + maxStack + " Locals: " + maxLocals);
        super.visitMaxs(maxStack, maxLocals);
    }

    public void print() {
        for (String instruction : this.instructions) {
            System.out.println(instruction);
        }
    }

    @Override
    public void visitEnd() {
//        this.print();
        super.visitEnd();
    }
}
