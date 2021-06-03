package net.yakclient.mixin.base.internal.bytecode

import net.yakclient.mixin.base.internal.methodadapter.MixinPatternMatcher
import net.yakclient.mixin.base.internal.methodadapter.PriorityMatcher
import net.yakclient.mixin.base.internal.methodadapter.tree.MethodProxyAdapter
import net.yakclient.mixin.base.internal.methodadapter.tree.TreeMethodProxy
import net.yakclient.mixin.base.internal.methodadapter.tree.TreeMixinPatternMatcher
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.tree.ClassNode
import java.util.*

class TreeMixinCV(
    private val parent: ClassVisitor,
    injectors: Map<String, Queue<QualifiedInstruction>>
) : MixinCV(
    ClassNode(), injectors
) {
    override fun visitMethod(
        access: Int,
        name: String,
        desc: String,
        signature: String?,
        exceptions: Array<String>?
    ): MethodVisitor {
        val visitor = super.visitMethod(access, name, desc, signature, exceptions)
        val qualifiedName = name + desc
        if (hasInjection(qualifiedName)) {
            val instructions = getInjection(qualifiedName)
            val nodes = PriorityQueue<PriorityMatcher<TreeMixinPatternMatcher>>();
            for (insn in instructions) {
               nodes.add(MixinPatternMatcher.createTreeNode(insn.injectionType, insn.insn, insn.priority));
            }
            return   MethodProxyAdapter(TreeMethodProxy(nodes), visitor)
        }
        return visitor
    }

    override fun visitEnd() {
        (cv as ClassNode).accept(parent)
    }
}