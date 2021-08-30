package net.yakclient.mixins.base

import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldNode
import org.objectweb.asm.tree.MethodNode
import java.util.*

abstract class InjectionMapper<T>(
    val transformers: List<InjectionTransformer<T>>
)

class ClassInjectionMapper(
    transformers: List<InjectionTransformer<ClassNode>> = LinkedList(),

    val fields: List<FieldInjectionMapper> = LinkedList(),
    val methods: List<MethodInjectionMapper> = LinkedList()
) : InjectionMapper<ClassNode>(transformers)

class FieldInjectionMapper(
    transformers: List<InjectionTransformer<FieldNode>> = LinkedList()
) : InjectionMapper<FieldNode>(transformers)

class MethodInjectionMapper(
    transformers: List<InjectionTransformer<MethodNode>> = LinkedList()
) : InjectionMapper<MethodNode>(transformers)

//fun injectionMappingOf()