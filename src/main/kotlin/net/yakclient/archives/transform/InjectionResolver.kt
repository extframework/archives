package net.yakclient.archives.transform

import org.objectweb.asm.*
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldNode
import org.objectweb.asm.tree.MethodNode

internal interface InjectionResolver

internal class ClassResolver(
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
        name: String,
        descriptor: String,
        signature: String?,
        value: Any?
    ): FieldVisitor =
        FieldResolver(delegate, config.ft, access, name, descriptor, signature, value)

    override fun visitEnd() {
        super.visitEnd()

        val visitor = cv as ClassNode

        config.ct(visitor)

        visitor.accept(delegate)
    }
}

internal class MethodResolver(
    private val delegate: MethodNode,
    private val transformer: MethodTransformer
) : MethodVisitor(
    Opcodes.ASM9, MethodNode(
        delegate.access,
        delegate.name,
        delegate.desc,
        delegate.signature,
        delegate.exceptions.toTypedArray()
    )
), InjectionResolver {
    override fun visitEnd() {
        super.visitEnd()

        val parent = mv as MethodNode
        transformer(parent)
        parent.accept(delegate)
    }
}

internal class FieldResolver(
    private val delegate: ClassVisitor,
    private val transformer: FieldTransformer,

    access: Int, name: String, descriptor: String, signature: String?, value: Any?
) : FieldVisitor(Opcodes.ASM9, FieldNode(access, name, descriptor, signature, value)), InjectionResolver {
    override fun visitEnd() {
        super.visitEnd()

        val parent = fv as FieldNode
        transformer(parent)
        parent.accept(delegate)
    }
}