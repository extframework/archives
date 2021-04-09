package net.yakclient.mixin.internal.instruction.core;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.objectweb.asm.Opcodes.*;

public class ProxyCoreInsnInterceptor extends CoreInsnInterceptor {
    private final UUID pointer;

    public ProxyCoreInsnInterceptor(UUID pointer, @NotNull String ownerDest, @NotNull String ownerSource) {
        super(ownerSource, ownerDest);
        this.pointer = pointer;
    }

    @Override
    public CoreInstruction intercept() {
       final List<ByteCodeConsumer> builder = new ArrayList<>();


        final Label l0 = new Label();
        final Label l1 = new Label();
        final Label l2 = new Label();
        final Label l3 = new Label();
        final Label l4 = new Label();

        builder.add(mv -> mv.visitLabel(l0));

        builder.add(mv -> mv.visitTypeInsn(NEW, "java/util/UUID"));
        builder.add(mv -> mv.visitInsn(DUP));
        builder.add(mv -> mv.visitLdcInsn(this.pointer.getMostSignificantBits()));
        builder.add(mv -> mv.visitLdcInsn(this.pointer.getLeastSignificantBits()));
        builder.add(mv -> mv.visitMethodInsn(INVOKESPECIAL, "java/util/UUID", "<init>", "(JJ)V", false));
        builder.add(mv -> mv.visitVarInsn(ASTORE, 1));

        builder.add(mv -> mv.visitLabel(l1));

        builder.add(mv -> mv.visitVarInsn(ALOAD, 1));
        builder.add(mv -> mv.visitMethodInsn(INVOKESTATIC, "net/yakclient/mixin/registry/proxy/MixinProxyManager", "proxy", "(Ljava/util/UUID;)Lnet/yakclient/mixin/registry/FunctionalProxy$ProxyResponseData;", false));
        builder.add(mv -> mv.visitVarInsn(ASTORE, 2));

        builder.add(mv -> mv.visitLabel(l2));

        builder.add(mv -> mv.visitVarInsn(ALOAD, 2));
        builder.add(mv -> mv.visitFieldInsn(GETFIELD, "net/yakclient/mixin/registry/FunctionalProxy$ProxyResponseData", "cancel", "Z"));

        builder.add(mv -> mv.visitJumpInsn(IFNE, l3));

        builder.add(mv -> mv.visitLabel(l4));

        builder.addAll(Arrays.asList(super.intercept().getActions()));

        builder.add(mv -> mv.visitLabel(l3));

        return new CoreInstruction(builder.toArray(new ByteCodeConsumer[0]));
    }

//    private CoreInstruction.InstructionBuilder applyProxy(int ln) {
//        CoreInstruction.InstructionBuilder builder = new CoreInstruction.InstructionBuilder();
//
//
//        final Label l0 = new Label();
//        final Label l1 = new Label();
//        final Label l2 = new Label();
//        final Label l3 = new Label();
//        final Label l4 = new Label();
//
////        label 0
//        builder.addInstruction(mv -> mv.visitLabel(l0));
//        builder.addInstruction(mv -> mv.visitLineNumber(ln + 1, l0));
//
//        builder.addInstruction(mv -> mv.visitTypeInsn(NEW, "java/util/UUID"));
//        builder.addInstruction(mv -> mv.visitInsn(DUP));
////
//        builder.addInstruction(mv -> mv.visitLdcInsn(this.pointer.getMostSignificantBits()));
//        builder.addInstruction(mv -> mv.visitLdcInsn(this.pointer.getLeastSignificantBits()));
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
//        builder.addAll(this.insn);
//
////        Label 3
//        builder.addInstruction(mv -> mv.visitLabel(l3));
//        builder.addInstruction(mv -> mv.visitLineNumber(ln + 3, l3));
////
//
//        return builder;
//    }
}
