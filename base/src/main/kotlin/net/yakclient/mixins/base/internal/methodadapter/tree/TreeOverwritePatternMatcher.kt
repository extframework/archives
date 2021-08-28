package net.yakclient.mixins.base.internal.methodadapter.tree

import net.yakclient.mixins.base.internal.instruction.Instruction
import org.objectweb.asm.tree.InsnList

/**
 * Defines the pattern for overwriting the method and inserting the mixin
 * as the new method.
 *
 *
 *
 * Note: This can cause serious issues with multiple mixins clashing. Use this
 * sparingly.
 *
 *
 *
 * Type: Overwrite
 *
 * @author Durgan McBroom
 */
class TreeOverwritePatternMatcher(instructions: Instruction?) : TreeMixinPatternMatcher(instructions) {
    override fun transform(insn: InsnList) {
        insn.clear()
        this.insertInsn(insn)
    }
}