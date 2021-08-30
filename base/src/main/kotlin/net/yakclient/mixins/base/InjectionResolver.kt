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
//    private val transformer: ClassTransformer,
//    private val methodTransformers: Queue<MethodTransformer>,
//    private val fieldTransformers: Queue<FieldTransformer>
) : ClassVisitor(Opcodes.ASM9, ClassNode()), InjectionResolver {
    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        var last: MethodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions) as MethodNode

        config.methodTransformers.forEach { last = MethodResolver(last, it) }
        return last
    }

    override fun visitField(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        value: Any?
    ): FieldVisitor {
        var last: FieldVisitor = super.visitField(access, name, descriptor, signature, value)

        config.fieldTransformers.forEach { last = FieldResolver(this, last as FieldNode, it) }
        return last
    }

    override fun visitEnd() {
        val visitor = cv as ClassNode

        config.classTransformers.forEach { it(visitor) }

        visitor.accept(delegate)

        super.visitEnd()
    }
}

class MethodResolver(
    private val delegate: MethodVisitor,
    private val transformer: MethodTransformer
) : MethodVisitor(Opcodes.ASM9, MethodNode()), InjectionResolver {
    override fun visitEnd() {
        val visitor = mv as MethodNode
        transformer(visitor)

        visitor.accept(delegate)
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