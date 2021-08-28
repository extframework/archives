package net.yakclient.mixins.base.internal.methodadapter.tree

import net.yakclient.mixins.base.internal.instruction.Instruction
import net.yakclient.mixins.base.internal.instruction.tree.InitialTreeInsnExecutor
import net.yakclient.mixins.base.internal.instruction.tree.PlacedTreeInsnExecutor
import net.yakclient.mixins.base.internal.methodadapter.MixinPatternMatcher
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.InsnList

abstract class TreeMixinPatternMatcher(private val mixinInsn: Instruction?) : MixinPatternMatcher {
    fun insertInsn(index: AbstractInsnNode?, list: InsnList) {
        if (index != null) PlacedTreeInsnExecutor(list, index, mixinInsn!!).execute()
    }

    fun insertInsn(list: InsnList?) {
        InitialTreeInsnExecutor(list!!, mixinInsn!!).execute()
    }

    abstract fun transform(insn: InsnList)
}