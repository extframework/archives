package net.yakclient.mixin.internal.instruction;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.UUID;

import static org.objectweb.asm.Opcodes.*;

public class ProxyMethodForwarder extends MethodInstructionForwarder {
    private final UUID pointer;
    private int ln = 0;

    public ProxyMethodForwarder(MethodVisitor mv, boolean shouldReturn, String ownerSource, String ownerDest, UUID pointer) {
        super(mv, shouldReturn, ownerSource, ownerDest);
        this.pointer = pointer;
    }

  /*
        //Code

        //This will be a constant
        final UUID identifier = new UUID(pointer.getUUID().getMostSignificantBits(), pointer.getUUID().getLeastSignificantBits());

        final FunctionalProxy.ProxyResponseData data = MixinProxyManager.proxy(identifier);
        //And include other conditionals
        if (!data.cancel)  {
            //Mixin Code
        }
*/

    //ByteCode conversion

    /*
L0
    LINENUMBER 36 L0
    NEW java/util/UUID
    DUP
    ALOAD 0
    GETFIELD net/yakclient/mixin/internal/instruction/ProxyMethodForwarder.pointer : Lnet/yakclient/mixin/registry/proxy/ProxiedPointer;
    INVOKEVIRTUAL net/yakclient/mixin/registry/proxy/ProxiedPointer.getUUID ()Ljava/util/UUID;
    INVOKEVIRTUAL java/util/UUID.getMostSignificantBits ()J
    ALOAD 0
    GETFIELD net/yakclient/mixin/internal/instruction/ProxyMethodForwarder.pointer : Lnet/yakclient/mixin/registry/proxy/ProxiedPointer;
    INVOKEVIRTUAL net/yakclient/mixin/registry/proxy/ProxiedPointer.getUUID ()Ljava/util/UUID;
    INVOKEVIRTUAL java/util/UUID.getLeastSignificantBits ()J
    INVOKESPECIAL java/util/UUID.<init> (JJ)V
    ASTORE 1
   L1
    LINENUMBER 38 L1
    ALOAD 1
    INVOKESTATIC net/yakclient/mixin/registry/proxy/MixinProxyManager.proxy (Ljava/util/UUID;)Lnet/yakclient/mixin/registry/FunctionalProxy$ProxyResponseData;
    ASTORE 2
   L2
    LINENUMBER 40 L2
    ALOAD 2
    GETFIELD nebuilder.addInstruction(mv->mvt/yakclient/mixin/registry/FunctionalProxy$ProxyResponseData.cancel : Z
    IFNE L3
   L4
    LINENUMBER 41 L4
    GETSTATIC java/lang/System.out : Ljava/io/PrintStream;
    LDC "YAYAY"
    INVOKEVIRTUAL java/io/PrintStream.println (Ljava/lang/String;)V
   L3
    LINENUMBER 44 L3
    FRAME APPEND [java/util/UUID net/yakclient/mixin/registry/FunctionalProxy$ProxyResponseData]
    RETURN
   L5
    LOCALVARIABLE this Lnet/yakclient/mixin/internal/instruction/ProxyMethodForwarder; L0 L5 0
    LOCALVARIABLE identifier Ljava/util/UUID; L1 L5 1
    LOCALVARIABLE data Lnet/yakclient/mixin/registry/FunctionalProxy$ProxyResponseData; L2 L5 2
    MAXSTACK = 6
    MAXLOCALS = 3
     */




    @Override
    public void visitLineNumber(int line, Label start) {
        this.ln = line;
        super.visitLineNumber(line, start);
    }

    @Override
    public Instruction toInstructions() {
        return this.applyProxy(this.ln).build();
    }

    private Instruction.InstructionBuilder applyProxy(int ln) {
        Instruction.InstructionBuilder builder = new Instruction.InstructionBuilder();


        final Label l0 = new Label();
        final Label l1 = new Label();
        final Label l2 = new Label();
        final Label l3 = new Label();
        final Label l4 = new Label();

//        label 0
        builder.addInstruction(mv -> mv.visitLabel(l0));
        builder.addInstruction(mv -> mv.visitLineNumber(ln + 1, l0));

        builder.addInstruction(mv -> mv.visitTypeInsn(NEW, "java/util/UUID"));
        builder.addInstruction(mv -> mv.visitInsn(DUP));
//
        builder.addInstruction(mv -> mv.visitLdcInsn(this.pointer.getMostSignificantBits()));
        builder.addInstruction(mv -> mv.visitLdcInsn(this.pointer.getLeastSignificantBits()));
//
        builder.addInstruction(mv -> mv.visitMethodInsn(INVOKESPECIAL, "java/util/UUID", "<init>", "(JJ)V", false));
        builder.addInstruction(mv -> mv.visitVarInsn(ASTORE, 1));

        //Label 1
        builder.addInstruction(mv -> mv.visitLabel(l1));
        builder.addInstruction(mv -> mv.visitLineNumber(ln + 2, l1));

        builder.addInstruction(mv -> mv.visitVarInsn(ALOAD, 1));
        builder.addInstruction(mv -> mv.visitMethodInsn(INVOKESTATIC, "net/yakclient/mixin/registry/proxy/MixinProxyManager", "proxy", "(Ljava/util/UUID;)Lnet/yakclient/mixin/registry/FunctionalProxy$ProxyResponseData;", false));
        builder.addInstruction(mv -> mv.visitVarInsn(ASTORE, 2));

        //Label 2
        builder.addInstruction(mv -> mv.visitLabel(l2));
        builder.addInstruction(mv -> mv.visitLineNumber(ln + 3, l2));

        builder.addInstruction(mv -> mv.visitVarInsn(ALOAD, 2));
        builder.addInstruction(mv -> mv.visitFieldInsn(GETFIELD, "net/yakclient/mixin/registry/FunctionalProxy$ProxyResponseData", "cancel", "Z"));

        builder.addInstruction(mv -> mv.visitJumpInsn(IFNE, l3));

        //Label 4
        builder.addInstruction(mv -> mv.visitLabel(l4));
        builder.addInstruction(mv -> mv.visitLineNumber(ln + 4, l4));

        builder.addAll(this.builder);

//        Label 3
        builder.addInstruction(mv -> mv.visitLabel(l3));
        builder.addInstruction(mv -> mv.visitLineNumber(ln + 3, l3));
//

        return builder;
    }
}
