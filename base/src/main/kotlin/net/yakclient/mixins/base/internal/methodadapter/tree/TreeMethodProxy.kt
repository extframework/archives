package net.yakclient.mixins.base.internal.methodadapter.tree

import net.yakclient.mixins.base.internal.bytecode.ByteCodeUtils
import net.yakclient.mixins.base.internal.methodadapter.MixinPatternMatcher
import net.yakclient.mixins.base.internal.methodadapter.PriorityMatcher
import org.objectweb.asm.tree.MethodNode
import java.util.*

class TreeMethodProxy(
    private val matchers: Queue<PriorityMatcher<TreeMixinPatternMatcher>>
    ) : MethodNode(ByteCodeUtils.ASM_VERSION), MixinPatternMatcher {

    private fun transform() {
        for (matcher in matchers) {
            matcher.patternMatcher.transform(instructions)
        }
    }

    override fun visitEnd() {
        super.visitEnd()
        transform()
    }

}