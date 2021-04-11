package net.yakclient.mixin.internal.methodadapter.tree;

import net.yakclient.YakMixins;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.MethodNode;

public class MethodProxyAdapter extends MethodVisitor {
    private final MethodVisitor parent;

    public MethodProxyAdapter(ASMMethodProxy proxy, MethodVisitor parent) {
        super(YakMixins.ASM_VERSION, proxy);
        this.parent = parent;
    }

    @Override
    public void visitEnd() {
        ((MethodNode) this.mv).accept(this.parent);
    }
}
