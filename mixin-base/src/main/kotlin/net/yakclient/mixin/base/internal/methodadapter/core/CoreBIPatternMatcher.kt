package net.yakclient.mixin.base.internal.methodadapter.core

import net.yakclient.mixin.base.internal.instruction.Instruction
import org.objectweb.asm.Handle
import org.objectweb.asm.MethodVisitor

class CoreBIPatternMatcher(
    visitor: MethodVisitor, instructions: Instruction
) : CoreMixinPatternMatcher(visitor, instructions) {

    override fun visitMethodInsn(opcode: Int, owner: String, name: String, desc: String) {
        this.executeInsn()
        super.visitMethodInsn(opcode, owner, name, desc)
    }

    override fun visitMethodInsn(opcode: Int, owner: String, name: String, desc: String, itf: Boolean) {
        this.executeInsn()
        super.visitMethodInsn(opcode, owner, name, desc, itf)
    }

    override fun visitInvokeDynamicInsn(name: String, desc: String, bsm: Handle, vararg bsmArgs: Any) {
        this.executeInsn()
        super.visitInvokeDynamicInsn(name, desc, bsm, *bsmArgs)
    }

    override fun visitInsn() {}
}