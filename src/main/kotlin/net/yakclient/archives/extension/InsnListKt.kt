@file:JvmName("InsnListKt")

package net.yakclient.archives.extension

import org.objectweb.asm.tree.*

public fun InsnList(instructions: List<AbstractInsnNode>): InsnList =
    InsnList().also { l -> instructions.forEach(l::add) }

public fun InsnList.slice(range: IntRange): InsnList = InsnList(toList().slice(range))

public fun InsnList.slice(first: Int): InsnList = InsnList(toList().slice(first until size()))

public fun InsnList.slice(first: AbstractInsnNode): InsnList = slice(indexOf(first))

public operator fun InsnList.plus(other: InsnList) : InsnList = InsnList(this.toList() + other.toList())