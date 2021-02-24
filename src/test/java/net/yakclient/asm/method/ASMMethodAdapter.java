package net.yakclient.asm.method;

import net.yakclient.mixin.internal.instruction.Instruction;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.UUID;

public class ASMMethodAdapter extends MethodVisitor {
    private int ln = 0;
    public ASMMethodAdapter() {
        super(Opcodes.ASM6);
    }

    public ASMMethodAdapter(MethodVisitor mv) {
        super(Opcodes.ASM6, mv);
    }

    @Override
    public void visitLineNumber(int line, Label start) {
        this.ln = line;
        super.visitLineNumber(line, start);
    }

    @Override
    public void visitCode() {
        super.visitCode();

        final UUID uuid = UUID.randomUUID();
        Instruction.InstructionBuilder builder = new Instruction.InstructionBuilder();

        final Label l0 = new Label();
        final Label l1 = new Label();
        final Label l2 = new Label();
        final Label l3 = new Label();
        final Label l4 = new Label();

        //label 0
        builder.addInstruction(mv -> mv.visitLabel(l0));
        builder.addInstruction(mv -> mv.visitLineNumber(ln + 1, l0));

        builder.addInstruction(mv -> mv.visitTypeInsn(Opcodes.NEW, "java/util/UUID"));
        builder.addInstruction(mv -> mv.visitInsn(Opcodes.DUP));

//        builder.addInstruction(mv -> mv.visitVarInsn(Opcodes.ALOAD, 0));
        builder.addInstruction(mv -> mv.visitLdcInsn(uuid.getMostSignificantBits()));
        builder.addInstruction(mv -> mv.visitLdcInsn(uuid.getLeastSignificantBits()));

        builder.addInstruction(mv -> mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/util/UUID", "<init>", "(JJ)V", false));
        builder.addInstruction(mv -> mv.visitVarInsn(Opcodes.ASTORE, 1));

        //Label 1
        builder.addInstruction(mv -> mv.visitLabel(l1));
        builder.addInstruction(mv -> mv.visitLineNumber(ln + 2, l1));

        builder.addInstruction(mv -> mv.visitVarInsn(Opcodes.ALOAD, 1));
        builder.addInstruction(mv -> mv.visitMethodInsn(Opcodes.INVOKESTATIC, "net/yakclient/mixin/registry/proxy/MixinProxyManager", "proxy", "(Ljava/util/UUID;)Lnet/yakclient/mixin/registry/FunctionalProxy$ProxyResponseData;", false));
        builder.addInstruction(mv -> mv.visitVarInsn(Opcodes.ASTORE, 2));

        //Label 2
        builder.addInstruction(mv -> mv.visitLabel(l2));
        builder.addInstruction(mv -> mv.visitLineNumber(ln + 3, l2));

        builder.addInstruction(mv -> mv.visitVarInsn(Opcodes.ALOAD, 2));
        builder.addInstruction(mv -> mv.visitFieldInsn(Opcodes.GETFIELD, "net/yakclient/mixin/registry/FunctionalProxy$ProxyResponseData", "cancel", "Z"));

        builder.addInstruction(mv -> mv.visitJumpInsn(Opcodes.IFNE, l3));


        //Label 4
        builder.addInstruction(mv -> mv.visitLabel(l4));
        builder.addInstruction(mv -> mv.visitLineNumber(ln + 4, l4));

        builder.addInstruction(mv->mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
        builder.addInstruction(mv->mv.visitLdcInsn("YAY YAY"));
        builder.addInstruction(mv->mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false));

        //Label 3
        builder.addInstruction(mv -> mv.visitLabel(l3));
        builder.addInstruction(mv -> mv.visitLineNumber(ln + 3, l3));

        builder.build().execute(this);

    }
}
