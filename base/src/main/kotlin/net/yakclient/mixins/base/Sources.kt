package net.yakclient.mixins.base

import org.objectweb.asm.ClassReader
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.MethodNode
import java.lang.reflect.Method
import kotlin.reflect.KFunction
import kotlin.reflect.jvm.javaMethod

object Sources {
    fun sourceOf(method: Method): InstructionResolver =
        MethodSourceResolver(method.declaringClass.name, ByteCodeUtils.jvmName(method))
//        val classReader = ClassReader(method.declaringClass.name)
//
//        val node = object : ClassNode(Opcodes.ASM9) {
//            lateinit var insn: InsnList
//
//            override fun visitMethod(
//                access: Int,
//                name: String,
//                descriptor: String,
//                signature: String?,
//                exceptions: Array<out String>?
//            ): MethodVisitor {
//                val mv = super.visitMethod(access, name, descriptor, signature, exceptions)
//
//                if (ByteCodeUtils.sameSignature(name + descriptor, ByteCodeUtils.jvmName(method)))
//                    insn = (mv as? MethodNode)?.instructions
//                        ?: throw IllegalStateException("Given class visitor must be a class node!")
//
//                return mv
//            }
//        }
//        classReader.accept(node, 0)
//
//        Opcodes.RETURN
//
//        return node.insn

    fun sourceOf(func: KFunction<*>) =
        sourceOf(requireNotNull(func.javaMethod) { "KFunction must have associated java method" })

    class MethodSourceResolver internal constructor(
        parent: String,
        signature: String
    ) : InstructionReader(parent, signature) {
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
