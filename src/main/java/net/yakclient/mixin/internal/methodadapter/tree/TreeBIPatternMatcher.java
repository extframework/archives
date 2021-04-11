package net.yakclient.mixin.internal.methodadapter.tree;

import net.yakclient.mixin.internal.instruction.ASMInstruction;
import net.yakclient.mixin.internal.instruction.Instruction;
import org.objectweb.asm.tree.InsnList;

public class TreeBIPatternMatcher extends TreeMixinPatternMatcher {
    public TreeBIPatternMatcher(Instruction instructions) {
        super(instructions);
    }

    @Override
    public void transform(InsnList insn) {

    }
}
