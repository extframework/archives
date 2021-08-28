package net.yakclient.mixins.base.internal.methodadapter.tree

import net.yakclient.mixins.base.internal.bytecode.ByteCodeUtils.isReturn
import net.yakclient.mixins.base.internal.instruction.Instruction
import org.objectweb.asm.tree.InsnList

/**
 * Defines the pattern for inserting mixins before every return
 * statement in the specified instructions.
 *
 *
 * Type: Before Return
 *
 * @author Durgan McBroom
 */
class TreeBRPatternMatcher(
    instructions: Instruction
) : TreeMixinPatternMatcher(instructions) {
    override fun transform(insn: InsnList) {
        for (node in insn) {
            if (isReturn(node.opcode)) this.insertInsn(node, insn)
        }
    }
}