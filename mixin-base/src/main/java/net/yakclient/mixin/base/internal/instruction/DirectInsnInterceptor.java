package net.yakclient.mixin.base.internal.instruction;

import net.yakclient.mixin.base.internal.bytecode.ByteCodeUtils;
import org.objectweb.asm.tree.MethodNode;

public class DirectInsnInterceptor extends MethodNode implements InstructionInterceptor {
    public DirectInsnInterceptor() {
        super(ByteCodeUtils.ASM_VERSION);
    }

    @Override
    public Instruction intercept() {
        return new DirectInstruction(this.instructions);
    }
}
