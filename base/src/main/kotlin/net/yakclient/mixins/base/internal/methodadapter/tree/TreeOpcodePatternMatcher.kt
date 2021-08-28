package net.yakclient.mixins.base.internal.methodadapter.tree

import net.yakclient.mixins.base.internal.instruction.Instruction
import org.objectweb.asm.tree.InsnList

/**
 * Defines the pattern for inserting mixins before the opcode specified.
 *
 *
 * Type: Before Opcode
 *
 * @author Durgan McBroom
 */
class TreeOpcodePatternMatcher(instructions: Instruction?, private val opcode: Int) :
    TreeMixinPatternMatcher(instructions) {
    override fun transform(insn: InsnList) {
        for (node in insn) {
            if (node.opcode == opcode) this.insertInsn(node, insn)
        }
    }
}