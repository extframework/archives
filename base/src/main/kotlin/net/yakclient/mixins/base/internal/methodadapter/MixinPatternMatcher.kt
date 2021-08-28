package net.yakclient.mixins.base.internal.methodadapter

import net.yakclient.mixins.api.InjectionType
import net.yakclient.mixins.base.internal.instruction.Instruction
import net.yakclient.mixins.base.internal.methodadapter.core.*
import net.yakclient.mixins.base.internal.methodadapter.tree.*
import org.objectweb.asm.MethodVisitor

interface MixinPatternMatcher {
    companion object {
        fun createCoreNode(type: Int, parent: MethodVisitor, insn: Instruction): CoreMixinPatternMatcher {
            return when (type) {
                InjectionType.AFTER_BEGIN -> CoreABPatternMatcher(parent, insn)
                InjectionType.BEFORE_INVOKE -> CoreBIPatternMatcher(parent, insn)
                InjectionType.BEFORE_RETURN -> CoreBRPatternMatcher(parent, insn)
                InjectionType.BEFORE_END -> CoreBEPatternMatcher(parent, insn)
                InjectionType.OVERWRITE -> CoreOverwritePatternMatcher(parent, insn)
                else -> CoreOpcodePatternMatcher(parent, insn, type)
            }
        }

        fun createTreeNode(type: Int, insn: Instruction, priority: Int): PriorityMatcher<TreeMixinPatternMatcher> {
            return PriorityMatcher(
                priority,
                when (type) {
                    InjectionType.AFTER_BEGIN -> TreeABPatternMatcher(insn)
                    InjectionType.BEFORE_INVOKE -> TreeBIPatternMatcher(insn)
                    InjectionType.BEFORE_RETURN -> TreeBRPatternMatcher(insn)
                    InjectionType.BEFORE_END -> TreeBEPatternMatcher(insn)
                    InjectionType.OVERWRITE -> TreeOverwritePatternMatcher(insn)
                    else -> TreeOpcodePatternMatcher(insn, type)
                }
            )
        }
    }
}