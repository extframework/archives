package net.yakclient.mixin.internal.instruction.adapter;

import net.yakclient.mixin.internal.instruction.InsnAdapter;
import net.yakclient.mixin.internal.instruction.Instruction;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;

public class ReturnRemoverInsnAdapter extends InsnAdapter {
    @Override
    public Instruction adapt(Instruction instruction) {
        final AbstractInsnNode lr = this.recursivelyFindReturn(instruction.getInstructions().getLast());
        final AbstractInsnNode last = lr == null ? instruction.getInstructions().getLast() : lr;

        if (last.getOpcode() == Opcodes.RETURN) instruction.getInstructions().remove(last);
        return super.adapt(instruction);
    }

    @Nullable
    private AbstractInsnNode recursivelyFindReturn(AbstractInsnNode node) {
        if (node.getOpcode() == Opcodes.RETURN) return node;
        if (node.getPrevious() == null) return null;
        return this.recursivelyFindReturn(node.getPrevious());
    }
}
