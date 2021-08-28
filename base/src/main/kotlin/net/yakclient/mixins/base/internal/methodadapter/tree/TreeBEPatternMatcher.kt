package net.yakclient.mixins.base.internal.methodadapter.tree

import net.yakclient.mixins.base.internal.bytecode.ByteCodeUtils.isReturn
import net.yakclient.mixins.base.internal.instruction.Instruction
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.InsnList

/**
 * Defines the pattern for inserting mixins before the last return of
 * the specified instructions.
 *
 *
 * Type: Before end
 *
 * @author Durgan McBroom
 */
class TreeBEPatternMatcher(
    instructions: Instruction
) : TreeMixinPatternMatcher(instructions) {

    override fun transform(insn: InsnList) {
        var lastReturn: AbstractInsnNode? = null
        for (node in insn) {
            if (isReturn(node.opcode)) lastReturn = node
        }
        this.insertInsn(lastReturn, insn)
    }
}