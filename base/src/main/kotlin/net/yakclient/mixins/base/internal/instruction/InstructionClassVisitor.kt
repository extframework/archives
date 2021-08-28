package net.yakclient.mixins.base.internal.instruction

import net.yakclient.mixins.base.internal.bytecode.ByteCodeUtils
import net.yakclient.mixins.base.internal.bytecode.ByteCodeUtils.descriptorsSame
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.tree.ClassNode

class InstructionClassVisitor(
    private val interceptor: InstructionInterceptor,
    private val targetMethod: String
) : ClassVisitor(ByteCodeUtils.ASM_VERSION, ClassNode()) {

    val instructions: Instruction
        get() = interceptor.intercept()

    override fun visitMethod(
        access: Int,
        name: String,
        desc: String,
        signature: String?,
        exceptions: Array<String>?
    ): MethodVisitor {
        val visitor = super.visitMethod(access, name, desc, signature, exceptions)
        if (visitor != null && descriptorsSame(this.targetMethod, name + desc)) {
            require(this.interceptor is MethodVisitor) { "InstructionInterceptor must inherit from the MethodVisitor" }
            return this.interceptor
        }
        return visitor
    }
}