package net.yakclient.mixin.base.internal.methodadapter.core

import net.yakclient.mixin.base.internal.bytecode.ByteCodeUtils
import net.yakclient.mixin.base.internal.bytecode.ByteCodeUtils.isLocalsStore
import net.yakclient.mixin.base.internal.instruction.Instruction
import net.yakclient.mixin.base.internal.instruction.core.CoreInsnExecutor
import net.yakclient.mixin.base.internal.methodadapter.MixinPatternMatcher
import org.objectweb.asm.MethodVisitor

abstract class CoreMixinPatternMatcher(visitor: MethodVisitor, private val instructions: Instruction) :
    MethodVisitor(ByteCodeUtils.ASM_VERSION, visitor), MixinPatternMatcher {
    var state = NOT_MATCHED
//    private var locals = 1
    fun executeInsn() {
        this.executeInsn(instructions)
    }

    private fun executeInsn(insn: Instruction?) {
        if (mv is CoreMixinPatternMatcher) (mv as CoreMixinPatternMatcher).executeInsn(insn) else CoreInsnExecutor(
            insn!!, mv
        ).execute()
    }

    override fun visitInsn(opcode: Int) {
        this.visitInsn()
        super.visitInsn(opcode)
    }

    override fun visitIntInsn(opcode: Int, operand: Int) {
        this.visitInsn()
        super.visitIntInsn(opcode, operand)
    }

    override fun visitVarInsn(opcode: Int, `var`: Int) {
        if (mv is CoreMixinPatternMatcher) super.visitVarInsn(
            opcode,
            `var`
        )
    }

    abstract fun visitInsn()

    companion object {
        const val NOT_MATCHED = 0
    }
}