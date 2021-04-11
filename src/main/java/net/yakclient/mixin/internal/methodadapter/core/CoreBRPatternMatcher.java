package net.yakclient.mixin.internal.methodadapter.core;

import net.yakclient.mixin.internal.bytecode.ByteCodeUtils;
import net.yakclient.mixin.internal.instruction.Instruction;
import org.objectweb.asm.MethodVisitor;

public class CoreBRPatternMatcher extends CoreMixinPatternMatcher {
    public CoreBRPatternMatcher(MethodVisitor visitor, Instruction instructions) {
        super(visitor, instructions);
    }

    @Override
    public void visitInsn(int opcode) {
        if (ByteCodeUtils.isReturn(opcode)) this.executeInsn();
        super.visitInsn(opcode);
    }

    @Override
    public void visitInsn() {
    }
}
