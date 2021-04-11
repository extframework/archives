package net.yakclient.mixin.internal.instruction;

import net.yakclient.YakMixins;
import org.objectweb.asm.tree.MethodNode;

public class ASMInsnInterceptor extends MethodNode implements InstructionInterceptor {
    public ASMInsnInterceptor() {
        super(YakMixins.ASM_VERSION);
    }

    @Override
    public Instruction intercept() {
        return new ASMInstruction(this.instructions);
    }
}
