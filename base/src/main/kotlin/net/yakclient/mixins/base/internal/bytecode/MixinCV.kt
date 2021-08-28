package net.yakclient.mixins.base.internal.bytecode

import org.objectweb.asm.ClassVisitor
import java.util.*

open class MixinCV(cv: ClassVisitor?, private val injectors: Map<String, Queue<QualifiedInstruction>>) :
    ClassVisitor(ByteCodeUtils.ASM_VERSION, cv) {
    protected fun hasInjection(name: String?): Boolean {
        for (s in injectors.keys) {
            if (ByteCodeUtils.descriptorsSame(name, s)) return true
        }
        return false
    }

    protected fun getInjection(name: String?): Queue<QualifiedInstruction> {
        for (s in injectors.keys) {
            if (ByteCodeUtils.descriptorsSame(name, s)) return injectors[s]!!
        }
        throw IllegalArgumentException("Failed to find injection")
    }
}