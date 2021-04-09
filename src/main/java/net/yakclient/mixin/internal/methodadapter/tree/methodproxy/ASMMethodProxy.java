package net.yakclient.mixin.internal.methodadapter.tree.methodproxy;

import net.yakclient.YakMixins;
import net.yakclient.mixin.internal.methodadapter.MixinPatternMatcher;
import net.yakclient.mixin.internal.methodadapter.PriorityMatcher;
import net.yakclient.mixin.internal.methodadapter.tree.TreeMixinPatternMatcher;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import java.util.PriorityQueue;
import java.util.Queue;

public abstract class ASMMethodProxy extends MethodNode implements MixinPatternMatcher {
    private final Queue<PriorityMatcher<TreeMixinPatternMatcher>> matchers;

    public ASMMethodProxy(Queue<PriorityMatcher<TreeMixinPatternMatcher>> matchers) {
        super(YakMixins.ASM_VERSION);
        this.matchers = matchers;
    }

    public void transform(InsnList instructions) {

    }

    public static class MethodProxyBuilder {
        private final Queue<PriorityMatcher<TreeMixinPatternMatcher>> matchers;

        public MethodProxyBuilder() {
            this.matchers = new PriorityQueue<>();
        }
    }

}


