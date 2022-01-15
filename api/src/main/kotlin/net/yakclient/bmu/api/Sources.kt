package net.yakclient.bmu.api

import org.objectweb.asm.ClassReader
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.MethodNode
import java.lang.reflect.Method
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.javaMethod


public object Sources {
    public fun sourceOf(method: Method): InstructionResolver =
        MethodSourceResolver(method.declaringClass.name, ByteCodeUtils.runtimeSignature(method))

    public fun sourceOf(func: KFunction<*>) : InstructionResolver =
        sourceOf(requireNotNull(func.javaMethod) { "KFunction must have associated java method" })

    public class MethodSourceResolver internal constructor(
        parent: String,
        signature: String
    ) : DirectInstructionReader(parent, signature) {
        override fun get(): InsnList {
            val classReader = ClassReader(parentClass)

            val node = object : ClassNode(Opcodes.ASM9) {
                lateinit var insn: InsnList

                override fun visitMethod(
                    access: Int,
                    name: String,
                    descriptor: String,
                    signature: String?,
                    exceptions: Array<out String>?
                ): MethodVisitor {
                    val mv = super.visitMethod(access, name, descriptor, signature, exceptions)

                    if (ByteCodeUtils.sameSignature(name + descriptor, methodSignature))
                        insn = (mv as? MethodNode)?.instructions
                            ?: throw IllegalStateException("Given class visitor must be a class node!")

                    return mv
                }
            }
            classReader.accept(node, 0)

            return node.insn
        }
    }
}
