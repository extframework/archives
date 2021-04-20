package net.yakclient.mixin.base.internal.methodadapter.core;

import net.yakclient.mixin.base.internal.instruction.Instruction;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;

public class CoreBIPatternMatcher extends CoreMixinPatternMatcher {
    public CoreBIPatternMatcher(MethodVisitor visitor, Instruction instructions) {
        super(visitor, instructions);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        this.executeInsn();
        super.visitMethodInsn(opcode, owner, name, desc);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        this.executeInsn();
        super.visitMethodInsn(opcode, owner, name, desc, itf);
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
        this.executeInsn();
        super.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
    }

    @Override
    public void visitInsn() {
    }
}
