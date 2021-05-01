package net.yakclient.mixin.base.internal.methodadapter.tree;

import net.yakclient.mixin.base.internal.instruction.Instruction;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;

/**
 * Defines the pattern for inserting mixins before any method invokes.
 * <p>
 * Type: Before invoke
 *
 * @author Durgan McBroom
 */
public class TreeBIPatternMatcher extends TreeMixinPatternMatcher {
    public TreeBIPatternMatcher(Instruction instructions) {
        super(instructions);
    }

    @Override
    public void transform(InsnList insn) {
        for (AbstractInsnNode node : insn) {
            if (node instanceof MethodInsnNode) this.insertInsn(node, insn);
        }
    }
}
