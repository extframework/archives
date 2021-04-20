package net.yakclient.mixin.base.internal.methodadapter.tree;

import net.yakclient.mixin.base.internal.instruction.Instruction;
import org.objectweb.asm.tree.InsnList;

public class TreeOverwritePatternMatcher extends TreeMixinPatternMatcher {
    public TreeOverwritePatternMatcher(Instruction instructions) {
        super(instructions);
    }

    @Override
    public void transform(InsnList insn) {
        insn.clear();
        this.insertInsn(null, insn);
    }
}
