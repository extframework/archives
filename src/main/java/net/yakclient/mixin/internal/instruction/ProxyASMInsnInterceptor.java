package net.yakclient.mixin.internal.instruction;

import org.objectweb.asm.Label;
import org.objectweb.asm.tree.*;

import java.util.UUID;

import static org.objectweb.asm.Opcodes.*;

public class ProxyASMInsnInterceptor extends ASMInsnInterceptor {
    private final UUID pointer;

    public ProxyASMInsnInterceptor(UUID pointer) {
        this.pointer = pointer;
    }

    @Override
    public ASMInstruction intercept() {
        final InsnList insn = new InsnList();


        final var l0 = new LabelNode(new Label());
        final var l1 = new LabelNode(new Label());
        final var l2 = new LabelNode(new Label());
        final var l3 = new LabelNode(new Label());
        final var l4 = new LabelNode(new Label());


//        label 0
        insn.add(l0);
        insn.add(new TypeInsnNode(NEW, "java/util/UUID"));
        insn.add(new InsnNode(DUP));
        insn.add(new LdcInsnNode(this.pointer.getMostSignificantBits()));
        insn.add(new LdcInsnNode(this.pointer.getLeastSignificantBits()));
        insn.add(new MethodInsnNode(INVOKESPECIAL, "java/util/UUID", "<init>", "(JJ)V", false));
        insn.add(new VarInsnNode(ASTORE, 1));

        insn.add(l1);
        insn.add(new VarInsnNode(ALOAD, 1));
        insn.add(new MethodInsnNode(INVOKESTATIC, "net/yakclient/mixin/registry/proxy/MixinProxyManager", "proxy", "(Ljava/util/UUID;)Lnet/yakclient/mixin/registry/FunctionalProxy$ProxyResponseData;", false));
        insn.add(new VarInsnNode(ASTORE, 2));

        insn.add(l2);
        insn.add(new VarInsnNode(ALOAD, 2));
        insn.add(new FieldInsnNode(GETFIELD, "net/yakclient/mixin/registry/FunctionalProxy$ProxyResponseData", "cancel", "Z"));

        insn.add(new JumpInsnNode(IFNE, l3));
        insn.add(l4);

        insn.add(super.intercept().getInstructions());

        insn.add(l3);

        return new ASMInstruction(insn);
    }
}
