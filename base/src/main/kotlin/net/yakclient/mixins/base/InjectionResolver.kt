package net.yakclient.mixins.base

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.FieldVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldNode
import org.objectweb.asm.tree.MethodNode

interface InjectionResolver

class ClassResolver(
    private val delegate: ClassVisitor,

    private val config: TransformerConfig,
) : ClassVisitor(Opcodes.ASM9, ClassNode()), InjectionResolver {
    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor =
        MethodResolver(super.visitMethod(access, name, descriptor, signature, exceptions) as MethodNode, config.mt)

    override fun visitField(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        value: Any?
    ): FieldVisitor =
        FieldResolver(this, super.visitField(access, name, descriptor, signature, value) as FieldNode, config.ft)

    override fun visitEnd() {
        val visitor = cv as ClassNode

        config.ct(visitor)

        visitor.accept(delegate)

        super.visitEnd()
    }
}

class MethodResolver(
    parent: MethodNode,
    private val transformer: MethodTransformer
) : MethodVisitor(Opcodes.ASM9, parent), InjectionResolver {
    override fun visitEnd() {
        val visitor = mv as MethodNode
        transformer(visitor)

        visitor.accept(visitor)
        super.visitEnd()
    }
}

class FieldResolver(
    private val delegate: ClassVisitor,
    parent: FieldNode,
    private val transformer: FieldTransformer
) : FieldVisitor(Opcodes.ASM9, parent), InjectionResolver {
    override fun visitEnd() {
        val visitor = fv as FieldNode
        transformer(visitor)

        visitor.accept(delegate)
        super.visitEnd()
    }
}