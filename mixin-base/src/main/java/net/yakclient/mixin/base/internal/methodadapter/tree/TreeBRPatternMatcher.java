package net.yakclient.mixin.base.internal.methodadapter.tree;

import net.yakclient.mixin.base.internal.bytecode.ByteCodeUtils;
import net.yakclient.mixin.base.internal.instruction.Instruction;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

/**
 * Defines the pattern for inserting mixins before every return
 * statement in the specified instructions.
 * <p>
 * Type: Before Return
 *
 * @author Durgan McBroom
 */
public class TreeBRPatternMatcher extends TreeMixinPatternMatcher {
    public TreeBRPatternMatcher(Instruction instructions) {
        super(instructions);
    }

    @Override
    public void transform(InsnList insn) {
        for (AbstractInsnNode node : insn) {
            if (ByteCodeUtils.isReturn(node.getOpcode())) this.insertInsn(node, insn);
        }
    }
}
