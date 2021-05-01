package net.yakclient.mixin.base.internal.methodadapter.tree;

import net.yakclient.mixin.base.internal.instruction.Instruction;
import org.objectweb.asm.tree.InsnList;

/**
 * Defines the pattern for inserting mixins after the start of
 * the specified instructions.
 * <p>
 * Type: After Begin
 *
 * @author Durgan McBroom
 */
public class TreeABPatternMatcher extends TreeMixinPatternMatcher {
    public TreeABPatternMatcher(Instruction instructions) {
        super(instructions);
    }

    @Override
    public void transform(InsnList instructions) {
        this.insertInsn(instructions);
    }
}
