package net.yakclient.mixin.base.internal.methodadapter.core

import net.yakclient.mixin.base.internal.bytecode.ByteCodeUtils.isReturn
import net.yakclient.mixin.base.internal.instruction.Instruction
import org.objectweb.asm.MethodVisitor

class CoreBRPatternMatcher(
    visitor: MethodVisitor,
    instructions: Instruction
) : CoreMixinPatternMatcher(visitor, instructions) {

    override fun visitInsn(opcode: Int) {
        if (isReturn(opcode)) this.executeInsn()
        super.visitInsn(opcode)
    }

    override fun visitInsn() {}
}