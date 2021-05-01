package net.yakclient.mixin.base.internal.methodadapter.tree;

import net.yakclient.mixin.base.internal.instruction.Instruction;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

/**
 * Defines the pattern for inserting mixins before the opcode specified.
 * <p>
 * Type: Before Opcode
 *
 * @author Durgan McBroom
 */
public class TreeOpcodePatternMatcher extends TreeMixinPatternMatcher {
    private final int opcode;

    public TreeOpcodePatternMatcher(Instruction instructions, int opcode) {
        super(instructions);
        this.opcode = opcode;
    }

    @Override
    public void transform(InsnList insn) {
        for (AbstractInsnNode node : insn) {
            if (node.getOpcode() == this.opcode) this.insertInsn(node, insn);
        }
    }
}
