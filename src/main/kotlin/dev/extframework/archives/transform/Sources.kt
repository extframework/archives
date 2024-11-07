package dev.extframework.archives.transform

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
    public fun of(method: Method): InstructionResolver {
        val declaringClass = method.declaringClass
        val name = declaringClass.name
        val r = name.replace(
            '.',
            '/'
        ) + ".class"

        check(declaringClass.enclosingClass == null) { "Failed to retrieve sources from class: '$name'. Class must be top level!" }

        return MethodSourceResolver(
            ClassReader(
                declaringClass.classLoader.getResourceAsStream(r)
                    ?: throw IllegalStateException("Failed to load content of class: '$name'. Looking in classloader: '${declaringClass.classLoader}' for resource '$r.")
            ), ByteCodeUtils.runtimeSignature(method)
        )
    }

    public fun of(func: KFunction<*>): InstructionResolver =
        of(requireNotNull(func.javaMethod) { "KFunction must have associated java method" })

    public class MethodSourceResolver internal constructor(
        reader: ClassReader,
        signature: String
    ) : DirectInstructionReader(reader, signature) {
        override fun get(): InsnList {
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

                    if (ByteCodeUtils.overloads(name + descriptor, methodSignature))
                        insn = (mv as? MethodNode)?.instructions
                            ?: throw IllegalStateException("Given class visitor must be a class node!")

                    return mv
                }
            }
            reader.accept(node, 0)

            return node.insn
        }
    }
}
