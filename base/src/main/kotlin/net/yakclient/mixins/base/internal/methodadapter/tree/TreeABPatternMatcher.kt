package net.yakclient.mixins.base.internal.methodadapter.tree

import net.yakclient.mixins.base.internal.instruction.Instruction
import org.objectweb.asm.tree.InsnList

/**
 * Defines the pattern for inserting mixins after the start of
 * the specified instructions.
 *
 *
 * Type: After Begin
 *
 * @author Durgan McBroom
 */
class TreeABPatternMatcher(
    instructions: Instruction
) : TreeMixinPatternMatcher(instructions) {
    override fun transform(insn: InsnList) {
        this.insertInsn(insn)
    }
}