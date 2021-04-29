package net.yakclient.mixin.base.internal.instruction;

import net.yakclient.mixin.base.YakMixins;
import org.objectweb.asm.tree.MethodNode;

public class ASMInsnInterceptor extends MethodNode implements InstructionInterceptor {
    public ASMInsnInterceptor() {
        super(YakMixins.ASM_VERSION);
    }

    @Override
    public Instruction intercept() {
        return new DirectInstruction(this.instructions);
    }
}
