package net.yakclient.mixin.base.internal.instruction;

import net.yakclient.mixin.base.registry.FunctionalProxy;
import net.yakclient.mixin.base.registry.proxy.MixinProxyManager;
import org.objectweb.asm.Label;
import org.objectweb.asm.tree.*;

import java.util.UUID;

import static org.objectweb.asm.Opcodes.*;

public class ProxyInsnInterceptor extends DirectInsnInterceptor {
    private final UUID pointer;

    public ProxyInsnInterceptor(UUID pointer) {
        this.pointer = pointer;
    }

    @Override
    public DirectInstruction intercept() {
        final InsnList insn = new InsnList();

        final var l0 = new LabelNode(new Label());
        final var l1 = new LabelNode(new Label());
        final var l2 = new LabelNode(new Label());
        final var l3 = new LabelNode(new Label());
        final var l4 = new LabelNode(new Label());


        insn.add(l0);

        insn.add(new TypeInsnNode(NEW, "java/util/UUID"));
        insn.add(new InsnNode(DUP));
        insn.add(new LdcInsnNode(this.pointer.getMostSignificantBits()));
        insn.add(new LdcInsnNode(this.pointer.getLeastSignificantBits()));
        insn.add(new MethodInsnNode(INVOKESPECIAL, "java/util/UUID", "<init>", "(JJ)V", false));

        final String proxyResName = this.getRuntimeName(FunctionalProxy.ProxyResponseData.class);

        insn.add(l1);
        insn.add(new MethodInsnNode(INVOKESTATIC, this.getRuntimeName(MixinProxyManager.class), "proxy", "(Ljava/util/UUID;)L" + proxyResName + ";", false));

        insn.add(l2);
        insn.add(new FieldInsnNode(GETFIELD, proxyResName, "cancel", "Z"));

        insn.add(new JumpInsnNode(IFNE, l3));

        insn.add(l4);
        insn.add(super.intercept().getInstructions());
        insn.add(l3);

        return new DirectInstruction(insn);
    }

    private String getRuntimeName(Class<?> cls) {
        return cls.getName().replaceAll("\\.", "/");
    }
}
