package net.yakclient.mixins.base.internal.methodadapter.core

import net.yakclient.mixins.base.internal.bytecode.ByteCodeUtils.isReturn
import net.yakclient.mixins.base.internal.instruction.Instruction
import org.objectweb.asm.*

class CoreBEPatternMatcher(
    visitor: MethodVisitor,
    instructions: Instruction,
) : CoreMixinPatternMatcher(visitor, instructions) {
    private var returnType: Int = Opcodes.RETURN

    override fun visitInsn(opcode: Int) {
        if (isReturn(opcode)) {
            state = FOUND_RETURN
            returnType = opcode
            return
        }
        super.visitInsn(opcode)
    }

    override fun visitEnd() {
        //Pointless but is nice for cleanup
        if (state == FOUND_RETURN) state = FOUND_LAST_RETURN
        this.executeInsn()
        super.visitInsn(returnType)
        super.visitMaxs(0, 0) /* Should be calculated by the ClassWriter, otherwise it will throw a verify error */
        super.visitEnd()
    }

    override fun visitInsn() {
        if (state == FOUND_RETURN) {
            state = NOT_MATCHED
            this.visitInsn(returnType)
        }
    }

    override fun visitLocalVariable(
        name: String,
        descriptor: String,
        signature: String,
        start: Label,
        end: Label,
        index: Int
    ) { }

    override fun visitLocalVariableAnnotation(
        typeRef: Int,
        typePath: TypePath,
        start: Array<Label>,
        end: Array<Label>,
        index: IntArray,
        descriptor: String,
        visible: Boolean
    ): AnnotationVisitor? { return null }

    override fun visitFrame(
        type: Int,
        numLocal: Int,
        local: Array<Any>,
        numStack: Int,
        stack: Array<Any>
    ) { /* Should be calculated automatically so again we have no need to provide it */
    }

    override fun visitMaxs(maxStack: Int, maxLocals: Int) { /* We dont want Max's to be called before we inject */
    }

    companion object {
        private const val FOUND_RETURN = 1
        private const val FOUND_LAST_RETURN = 2
    }
}