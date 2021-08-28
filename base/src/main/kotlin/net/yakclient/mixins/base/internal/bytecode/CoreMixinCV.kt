package net.yakclient.mixins.base.internal.bytecode

import net.yakclient.mixins.base.internal.methodadapter.MixinPatternMatcher
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import java.util.*

class CoreMixinCV(
    visitor: ClassVisitor?,
    injectors: Map<String, Queue<QualifiedInstruction>>
) : MixinCV(visitor, injectors) {
    override fun visitMethod(
        access: Int,
        name: String,
        desc: String,
        signature: String?,
        exceptions: Array<String>?
    ): MethodVisitor {
        val visitor = super.visitMethod(access, name, desc, signature, exceptions)
        val qualifiedName = name + desc
        if (hasInjection(qualifiedName)) {
            var last = visitor
            val instructions = getInjection(qualifiedName)
            for (insn in instructions) {
                last = MixinPatternMatcher.createCoreNode(insn.injectionType, last, insn.insn)
            }
            return last
        }
        return visitor
    }
}