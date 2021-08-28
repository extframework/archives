@file:JvmName("ClassNodeKt")

package net.yakclient.mixins.base.extension

import net.yakclient.mixins.base.internal.bytecode.ByteCodeUtils
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.FieldNode
import org.objectweb.asm.tree.MethodNode


fun ClassNode.methodOf(name: String, vararg args: Class<*>): MethodNode? = methods.firstOrNull {
    it.name == name && args.joinToString(
        prefix = "(",
        postfix = ")",
        transform = ByteCodeUtils::toRuntimeName
    ) == it.desc
}

fun ClassNode.fieldOf(name: String): FieldNode? = fields.firstOrNull { it.name == name }

