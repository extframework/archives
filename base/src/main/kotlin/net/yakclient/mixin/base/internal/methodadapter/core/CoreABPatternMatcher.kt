package net.yakclient.mixin.base.internal.methodadapter.core

import net.yakclient.mixin.base.internal.instruction.Instruction
import org.objectweb.asm.MethodVisitor

class CoreABPatternMatcher(
    visitor: MethodVisitor,
    instructions: Instruction
) : CoreMixinPatternMatcher(visitor, instructions) {

    override fun visitInsn() {}

    override fun visitCode() {
        super.visitCode()
        this.executeInsn()
    }
}