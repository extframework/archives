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
//    override fun visit(
//        version: Int,
//        access: Int,
//        name: String?,
//        signature: String?,
//        superName: String?,
//        interfaces: Array<out String>?
//    ) {
//        println(version)
//        super.visit(version, access, name, signature, superName, interfaces)
//    }

    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor =
        MethodResolver(super.visitMethod(access, name, descriptor, signature, exceptions) as MethodNode, config.mt)

//    override fun visitField(
//        access: Int,
//        name: String?,
//        descriptor: String?,
//        signature: String?,
//        value: Any?
//    ): FieldVisitor =
//        FieldResolver(super.visitField(access, name, descriptor, signature, value) as FieldNode, delegate, config.ft)

    override fun visitEnd() {
        val visitor = cv as ClassNode

        config.ct(visitor)

        visitor.accept(delegate)

        super.visitEnd()
    }
}

class MethodResolver(
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

class FieldResolver(
    parent: FieldNode,
    private val delegate: ClassVisitor,
    private val transformer: FieldTransformer
) : FieldVisitor(Opcodes.ASM9, parent), InjectionResolver {
    override fun visitEnd() {
        super.visitEnd()

        val parent = fv as FieldNode
//        transformer(parent)
        parent.accept(delegate)
    }
}