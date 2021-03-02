package net.yakclient.apitests;

import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.Textifier;

import java.io.PrintWriter;

public class PrintTextifier extends Textifier {
    protected PrintTextifier() {
        super(Opcodes.ASM6);
    }

    @Override
    public void visitCode() {
        System.out.println("Visited code");
        print(new PrintWriter(System.out));
        //super.visitCode();
    }

    @Override
    public void visitInsn(int opcode) {
//                        Opcodes.
        System.out.println("Visited a opcode");
        print(new PrintWriter(System.out));
        //super.visitInsn(opcode);
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        System.out.println("Visited a opcode and a operand");
        print(new PrintWriter(System.out));
        //super.visitIntInsn(opcode, operand);
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        System.out.println("Visited a variable opcode");
        print(new PrintWriter(System.out));
        //super.visitVarInsn(opcode, var);
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        System.out.println("Visited a type opcode");
        print(new PrintWriter(System.out));
        //super.visitTypeInsn(opcode, type);
    }


    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        System.out.println("Visited a method instruction");
        print(new PrintWriter(System.out));
        //super.visitMethodInsn(opcode, owner, name, desc, itf);
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
        System.out.println("visited a dynamic invoke");
        print(new PrintWriter(System.out));
        //super.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        System.out.println("visited a jump instruction");
        print(new PrintWriter(System.out));
        //super.visitJumpInsn(opcode, label);
    }

    @Override
    public void visitLabel(Label label) {
        System.out.println("visited a label.");
        print(new PrintWriter(System.out));
        //super.visitLabel(label);
    }

    @Override
    public void visitLdcInsn(Object cst) {
        System.out.println("visited a LDC: obj: " + cst);
        print(new PrintWriter(System.out));
        //super.visitLdcInsn(cst);
    }

    @Override
    public void visitIincInsn(int var, int increment) {
        System.out.println("Visit a Iinc");
        print(new PrintWriter(System.out));
        //super.visitIincInsn(var, increment);
    }

    @Override
    public void visitMultiANewArrayInsn(String desc, int dims) {
        System.out.println("Visited a new array instruction");
        print(new PrintWriter(System.out));
        //super.visitMultiANewArrayInsn(desc, dims);
    }

//                    @Override
//                    public Printer visitInsnAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
//                        System.out.println("Visist");
//                        print(new PrintWriter(System.out));
//                        return //super.visitInsnAnnotation(typeRef, typePath, desc, visible);
//                    }

    @Override
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        System.out.println("Visited a try catch block");
        print(new PrintWriter(System.out));
        //super.visitTryCatchBlock(start, end, handler, type);
    }
//
//                    @Override
//                    public Printer visitTryCatchAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
//                        System.out.println("Visist");
//                        print(new PrintWriter(System.out));
//                        return //super.visitTryCatchAnnotation(typeRef, typePath, desc, visible);
//                    }

    @Override
    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        System.out.println("Visited a local variable instruction");
        print(new PrintWriter(System.out));
        //super.visitLocalVariable(name, desc, signature, start, end, index);
    }

//                    @Override
//                    public Printer visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String desc, boolean visible) {
//                        System.out.println("Visist");
//                        print(new PrintWriter(System.out));
//                        return //super.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, desc, visible);
//                    }

    @Override
    public void visitLineNumber(int line, Label start) {
        System.out.println("Visited a line number of: " + line);
        print(new PrintWriter(System.out));
        //super.visitLineNumber(line, start);
    }



    @Override
    public void visitMethodEnd() {
        System.out.println("END");
        print(new PrintWriter(System.out));
        //super.visitMethodEnd();
    }

//                    @Override
//                    public Textifier visitAnnotation(String desc, boolean visible) {
//                        System.out.println("Visist");
//                        print(new PrintWriter(System.out));
//                        return //super.visitAnnotation(desc, visible);
//                    }

//                    @Override
//                    public Textifier visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
//                        System.out.println("Visist");
//                        print(new PrintWriter(System.out));
////                        return  //super.visitTypeAnnotation(typeRef, typePath, desc, visible);
//                    }




}
