package net.yakclient.mixin.base.internal.methodadapter.tree

import net.yakclient.mixin.base.internal.instruction.Instruction
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.MethodInsnNode

/**
 * Defines the pattern for inserting mixins before any method invokes.
 *
 *
 * Type: Before invoke
 *
 * @author Durgan McBroom
 */
class TreeBIPatternMatcher(
    instructions: Instruction
) : TreeMixinPatternMatcher(instructions) {
    override fun transform(insn: InsnList) {
        for (node in insn) {
            if (node is MethodInsnNode) this.insertInsn(node, insn)
        }
    }
}