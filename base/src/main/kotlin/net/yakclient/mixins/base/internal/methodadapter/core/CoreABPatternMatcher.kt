package net.yakclient.mixins.base.internal.methodadapter.core

import net.yakclient.mixins.base.internal.instruction.Instruction
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