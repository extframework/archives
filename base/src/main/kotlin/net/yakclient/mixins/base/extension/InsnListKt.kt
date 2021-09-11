@file:JvmName("InsnListKt")

package net.yakclient.mixins.base.extension

import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.MethodNode

//fun InsnList.insertSafelyBefore(previous: AbstractInsnNode, insn: InsnList) {
//    var last: AbstractInsnNode? = previous.previous
//    for (node in insn) {
//        if (last == null) insert(last, node) else insert(node); last = node
//    }
////    for (insn: AbstractInsnNode; ) this.insertBefore(last, )// .forEach { this.insertBefore(last, it); last = it }
//}

fun InsnList.copy(): InsnList =
    MethodNode().also {  accept(it) }.instructions

//fun InsnList.setAfter(old: AbstractInsnNode, list: InsnList): InsnList = apply {
//    this.set(old, list.first)
//
//
//    var last = list.first
//    for (i in 1..list.size()) {
//        val node = list[i]
//        this.insert(last, node)
//        last = node
//    }
//}
//
//fun InsnList.setBefore(old: AbstractInsnNode, list: InsnList) : InsnList = apply {
//    this.set(old, list.last)
//
//    var first = list.last
//    for (i in list.size()..1) {
//
//    }
//}

//fun mixinExample() {
//    val list = InsnList()
//
//    list.insert(list.find { ByteCodeUtils.isReturn(it.opcode) }!!, InsnList())
//
//    list.set(list.find { it is VarInsnNode && it.next.next.opcode == Opcodes.NEW }!!, InsnList())
//}

