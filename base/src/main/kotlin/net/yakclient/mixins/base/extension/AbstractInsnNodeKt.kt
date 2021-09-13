@file:JvmName("AbstractInsnNodeKt")

package net.yakclient.mixins.base.extension

import org.objectweb.asm.tree.AbstractInsnNode

public fun AbstractInsnNode.hasNext() : Boolean = this.next != null

public fun AbstractInsnNode.hasPrevious() : Boolean = this.previous != null