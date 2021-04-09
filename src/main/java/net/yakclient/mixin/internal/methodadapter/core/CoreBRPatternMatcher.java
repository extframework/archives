package net.yakclient.mixin.internal.methodadapter.core;

import net.yakclient.mixin.internal.bytecode.ByteCodeUtils;
import net.yakclient.mixin.internal.bytecode.PriorityInstruction;
import net.yakclient.mixin.internal.instruction.core.CoreInstruction;
import org.objectweb.asm.MethodVisitor;

import java.util.Queue;

public class CoreBRPatternMatcher extends CoreMixinPatternMatcher {
    public CoreBRPatternMatcher(MethodVisitor visitor, CoreInstruction instructions) {
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
