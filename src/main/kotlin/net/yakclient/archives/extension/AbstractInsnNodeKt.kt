@file:JvmName("AbstractInsnNodeKt")

package net.yakclient.archives.extension

import org.objectweb.asm.tree.AbstractInsnNode

/**
 * Determines if the given AbstractInsnNode has a next.
 *
 * @receiver the abstract node to find the next of
 *
 * @return if there is a next in the insn list.
 */
public fun AbstractInsnNode.hasNext() : Boolean = this.next != null

/**
 * Determines if the given AbstractInsnNode has a previous.
 *
 * @receiver the abstract node to find the previous of
 *
 * @return if there is a previous in the insn list.
 */
public fun AbstractInsnNode.hasPrevious() : Boolean = this.previous != null