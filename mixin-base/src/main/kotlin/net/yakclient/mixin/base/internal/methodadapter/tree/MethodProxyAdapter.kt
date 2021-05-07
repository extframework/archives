package net.yakclient.mixin.base.internal.methodadapter.tree

import net.yakclient.mixin.base.internal.bytecode.ByteCodeUtils
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.tree.MethodNode

class MethodProxyAdapter(
    proxy: TreeMethodProxy,
    private val parent: MethodVisitor
) : MethodVisitor(ByteCodeUtils.ASM_VERSION, proxy) {

    override fun visitEnd() {
        super.visitEnd()
        (mv as MethodNode).accept(parent)
    }
}