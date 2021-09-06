@file:JvmName("AbstractInsnNodeKt")

package net.yakclient.mixins.base.extension

import org.objectweb.asm.tree.AbstractInsnNode

fun AbstractInsnNode.hasNext() = this.next != null

fun AbstractInsnNode.hasPrevious() = this.previous != null