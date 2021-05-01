package net.yakclient.mixin.base.internal.methodadapter.tree;

import net.yakclient.mixin.base.internal.bytecode.ByteCodeUtils;
import net.yakclient.mixin.base.internal.instruction.Instruction;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

/**
 * Defines the pattern for inserting mixins before the last return of
 * the specified instructions.
 * <p>
 * Type: Before end
 *
 * @author Durgan McBroom
 */
public class TreeBEPatternMatcher extends TreeMixinPatternMatcher {
    public TreeBEPatternMatcher(Instruction instructions) {
        super(instructions);
    }

    @Override
    public void transform(InsnList insn) {
        AbstractInsnNode lastReturn = null;
        for (AbstractInsnNode node : insn) {
            if (ByteCodeUtils.isReturn(node.getOpcode())) lastReturn = node;
        }
        this.insertInsn(lastReturn, insn);
    }
}
