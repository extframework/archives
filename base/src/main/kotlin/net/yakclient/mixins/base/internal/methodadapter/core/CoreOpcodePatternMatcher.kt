package net.yakclient.mixins.base.internal.methodadapter.core

import net.yakclient.mixins.base.internal.instruction.Instruction
import org.objectweb.asm.Handle
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class CoreOpcodePatternMatcher(
    visitor: MethodVisitor,
    instructions: Instruction,
    private val opcode: Int
) : CoreMixinPatternMatcher(visitor, instructions) {

    override fun visitVarInsn(opcode: Int, `var`: Int) {
        if (this.opcode == opcode) this.executeInsn()
        super.visitVarInsn(opcode, `var`)
    }

    override fun visitTypeInsn(opcode: Int, type: String) {
        if (this.opcode == opcode) this.executeInsn()
        super.visitTypeInsn(opcode, type)
    }

    override fun visitFieldInsn(opcode: Int, owner: String, name: String, descriptor: String) {
        if (this.opcode == opcode) this.executeInsn()
        super.visitFieldInsn(opcode, owner, name, descriptor)
    }

    override fun visitMethodInsn(opcode: Int, owner: String, name: String, descriptor: String) {
        if (this.opcode == opcode) this.executeInsn()
        super.visitMethodInsn(opcode, owner, name, descriptor)
    }

    override fun visitMethodInsn(opcode: Int, owner: String, name: String, descriptor: String, isInterface: Boolean) {
        if (this.opcode == opcode) this.executeInsn()
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface)
    }

    override fun visitInvokeDynamicInsn(
        name: String,
        descriptor: String,
        bootstrapMethodHandle: Handle,
        vararg bootstrapMethodArguments: Any
    ) {
        if (opcode == Opcodes.INVOKEDYNAMIC) this.executeInsn()
        super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, *bootstrapMethodArguments)
    }

    override fun visitJumpInsn(opcode: Int, label: Label) {
        if (this.opcode == opcode) this.executeInsn()
        super.visitJumpInsn(opcode, label)
    }

    override fun visitLdcInsn(value: Any) {
        if (opcode == Opcodes.LDC) this.executeInsn()
        super.visitLdcInsn(value)
    }

    override fun visitIincInsn(`var`: Int, increment: Int) {
        if (opcode == Opcodes.IINC) this.executeInsn()
        super.visitIincInsn(`var`, increment)
    }

    override fun visitTableSwitchInsn(min: Int, max: Int, dflt: Label, vararg labels: Label) {
        if (opcode == Opcodes.TABLESWITCH) this.executeInsn()
        super.visitTableSwitchInsn(min, max, dflt, *labels)
    }

    override fun visitLookupSwitchInsn(dflt: Label, keys: IntArray, labels: Array<Label>) {
        if (opcode == Opcodes.LOOKUPSWITCH) this.executeInsn()
        super.visitLookupSwitchInsn(dflt, keys, labels)
    }

    override fun visitMultiANewArrayInsn(descriptor: String, numDimensions: Int) {
        if (opcode == Opcodes.MULTIANEWARRAY) this.executeInsn()
        super.visitMultiANewArrayInsn(descriptor, numDimensions)
    }

    override fun visitInsn() {}
}