package net.yakclient.mixin.internal.instruction.tree;

import net.yakclient.mixin.internal.instruction.InstructionInterceptor;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class TreeInsnInterceptor extends MethodNode implements InstructionInterceptor<TreeInstruction> {
    @Override
    public TreeInstruction intercept() {
        return new TreeInstruction(this.instructions);
    }
}
