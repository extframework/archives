package net.yakclient.mixin.base.internal.methodadapter.core

import net.yakclient.mixin.base.internal.instruction.Instruction
import org.objectweb.asm.*

class CoreOverwritePatternMatcher(
    visitor: MethodVisitor,
    instructions: Instruction
) : CoreMixinPatternMatcher(visitor, instructions) {

    override fun visitCode() {
        super.visitCode()
        this.executeInsn()
    }

    override fun visitInsn() {}
    override fun visitVarInsn(opcode: Int, `var`: Int) {}
    override fun visitTypeInsn(opcode: Int, type: String) {}
    override fun visitFieldInsn(opcode: Int, owner: String, name: String, descriptor: String) {}
    override fun visitMethodInsn(opcode: Int, owner: String, name: String, descriptor: String) {}
    override fun visitMethodInsn(opcode: Int, owner: String, name: String, descriptor: String, isInterface: Boolean) {}
    override fun visitInvokeDynamicInsn(
        name: String,
        descriptor: String,
        bootstrapMethodHandle: Handle,
        vararg bootstrapMethodArguments: Any
    ) {
    }

    override fun visitJumpInsn(opcode: Int, label: Label) {}
    override fun visitLabel(label: Label) {}
    override fun visitLdcInsn(value: Any) {}
    override fun visitIincInsn(`var`: Int, increment: Int) {}
    override fun visitTableSwitchInsn(min: Int, max: Int, dflt: Label, vararg labels: Label) {}
    override fun visitLookupSwitchInsn(dflt: Label, keys: IntArray, labels: Array<Label>) {}
    override fun visitMultiANewArrayInsn(descriptor: String, numDimensions: Int) {}
    override fun visitInsnAnnotation(
        typeRef: Int,
        typePath: TypePath,
        descriptor: String,
        visible: Boolean
    ): AnnotationVisitor? {
        return null
    }

    override fun visitTryCatchBlock(start: Label, end: Label, handler: Label, type: String) {}
    override fun visitTryCatchAnnotation(
        typeRef: Int,
        typePath: TypePath,
        descriptor: String,
        visible: Boolean
    ): AnnotationVisitor? {
        return null
    }

    override fun visitLocalVariable(
        name: String,
        descriptor: String,
        signature: String,
        start: Label,
        end: Label,
        index: Int
    ) {
    }

    override fun visitLocalVariableAnnotation(
        typeRef: Int,
        typePath: TypePath,
        start: Array<Label>,
        end: Array<Label>,
        index: IntArray,
        descriptor: String,
        visible: Boolean
    ): AnnotationVisitor? {
        return null
    }

    override fun visitLineNumber(line: Int, start: Label) {}
}