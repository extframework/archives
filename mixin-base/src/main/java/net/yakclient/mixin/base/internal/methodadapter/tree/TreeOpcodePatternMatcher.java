package net.yakclient.mixin.base.internal.methodadapter.tree;

import net.yakclient.mixin.base.internal.instruction.Instruction;
import org.objectweb.asm.tree.InsnList;

public class TreeOpcodePatternMatcher extends TreeMixinPatternMatcher {
    private final int opcode;

    public TreeOpcodePatternMatcher(Instruction instructions, int opcode) {
        super(instructions);
        this.opcode = opcode;
    }

    @Override
    public void transform(InsnList insn) {

    }
}
