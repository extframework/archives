package net.yakclient.mixin.internal.methodadapter;

import net.yakclient.mixin.internal.instruction.Instruction;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;

import java.util.Queue;

public class BeforeInvokePatternMatcher extends MethodInjectionPatternMatcher {


    public BeforeInvokePatternMatcher(MethodVisitor visitor, Queue<Instruction> instructions) {
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
