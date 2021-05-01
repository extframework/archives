package net.yakclient.mixin.base.internal.methodadapter.tree;

import net.yakclient.mixin.base.internal.instruction.Instruction;
import org.objectweb.asm.tree.InsnList;

/**
 * Defines the pattern for overwriting the method and inserting the mixin
 * as the new method.
 * <p>
 *
 * Note: This can cause serious issues with multiple mixins clashing. Use this
 * sparingly.
 *
 * <p>
 * Type: Overwrite
 *
 * @author Durgan McBroom
 */
public class TreeOverwritePatternMatcher extends TreeMixinPatternMatcher {
    public TreeOverwritePatternMatcher(Instruction instructions) {
        super(instructions);
    }

    @Override
    public void transform(InsnList insn) {
        insn.clear();
        this.insertInsn(insn);
    }
}
