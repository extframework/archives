package net.yakclient.asm.method;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.*;

public class ASMMethodAdapter extends MethodVisitor {
    private int ln = 0;
    public ASMMethodAdapter() {
        super(Opcodes.ASM9);
    }

    public ASMMethodAdapter(MethodVisitor mv) {
        super(Opcodes.ASM9, mv);
    }

    @Override
    public void visitLineNumber(int line, Label start) {
        this.ln = line;
        super.visitLineNumber(line, start);
    }

    @Override
    public void visitCode() {
//        super.visitCode();
//
//        final UUID uuid = UUID.randomUUID();
//        CoreInstruction.InstructionBuilder builder = new CoreInstruction.InstructionBuilder();
//
//        final Label l0 = new Label();
//        final Label l1 = new Label();
//        final Label l2 = new Label();
//        final Label l3 = new Label();
//        final Label l4 = new Label();
//
//        //label 0
//        builder.addInstruction(mv -> mv.visitLabel(l0));
//        builder.addInstruction(mv -> mv.visitLineNumber(ln + 1, l0));
//
//        builder.addInstruction(mv -> mv.visitTypeInsn(NEW, "java/util/UUID"));
//        builder.addInstruction(mv -> mv.visitInsn(DUP));
////
//        builder.addInstruction(mv -> mv.visitLdcInsn(uuid.getMostSignificantBits()));
//        builder.addInstruction(mv -> mv.visitLdcInsn(uuid.getLeastSignificantBits()));
////
//        builder.addInstruction(mv -> mv.visitMethodInsn(INVOKESPECIAL, "java/util/UUID", "<init>", "(JJ)V", false));
//        builder.addInstruction(mv -> mv.visitVarInsn(ASTORE, 1));
//
//        //Label 1
//        builder.addInstruction(mv -> mv.visitLabel(l1));
//        builder.addInstruction(mv -> mv.visitLineNumber(ln + 2, l1));
//
//        builder.addInstruction(mv -> mv.visitVarInsn(ALOAD, 1));
//        builder.addInstruction(mv -> mv.visitMethodInsn(INVOKESTATIC, "net/yakclient/mixin/registry/proxy/MixinProxyManager", "proxy", "(Ljava/util/UUID;)Lnet/yakclient/mixin/registry/FunctionalProxy$ProxyResponseData;", false));
//        builder.addInstruction(mv -> mv.visitVarInsn(ASTORE, 2));
//
//        //Label 2
//        builder.addInstruction(mv -> mv.visitLabel(l2));
//        builder.addInstruction(mv -> mv.visitLineNumber(ln + 3, l2));
//
//        builder.addInstruction(mv -> mv.visitVarInsn(ALOAD, 2));
//        builder.addInstruction(mv -> mv.visitFieldInsn(GETFIELD, "net/yakclient/mixin/registry/FunctionalProxy$ProxyResponseData", "cancel", "Z"));
//
//        builder.addInstruction(mv -> mv.visitJumpInsn(IFNE, l3));
//
//        //Label 4
//        builder.addInstruction(mv -> mv.visitLabel(l4));
//        builder.addInstruction(mv -> mv.visitLineNumber(ln + 4, l4));
//
//        builder.addInstruction(mv->mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
//        builder.addInstruction(mv->mv.visitLdcInsn("YAY YAY"));
//        builder.addInstruction(mv->mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false));
//
//        builder.addInstruction(mv -> mv.visitLabel(l3));
//        builder.addInstruction(mv -> mv.visitLineNumber(ln + 3, l3));
//
////        builder.addInstruction(mv->mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
////        builder.addInstruction(mv->mv.visitLdcInsn("This is where we do print stuff"));
////        builder.addInstruction(mv->mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false));
//
//        builder.build().execute(this);

    }
}
