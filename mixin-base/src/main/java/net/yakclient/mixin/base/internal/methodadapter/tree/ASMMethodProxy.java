package net.yakclient.mixin.base.internal.methodadapter.tree;

import net.yakclient.mixin.base.YakMixins;
import net.yakclient.mixin.base.internal.methodadapter.MixinPatternMatcher;
import net.yakclient.mixin.base.internal.methodadapter.PriorityMatcher;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.MethodNode;

import java.util.PriorityQueue;
import java.util.Queue;

public class ASMMethodProxy extends MethodNode implements MixinPatternMatcher {
    private final Queue<PriorityMatcher<TreeMixinPatternMatcher>> matchers;

    public ASMMethodProxy(Queue<PriorityMatcher<TreeMixinPatternMatcher>> matchers) {
        super(YakMixins.ASM_VERSION);
        this.matchers = matchers;
    }

    public void transform() {
        for (PriorityMatcher<TreeMixinPatternMatcher> matcher : this.matchers) {
            matcher.get().transform(this.instructions);
        }
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
        this.transform();
    }

    public static class MethodProxyBuilder {
        private final Queue<PriorityMatcher<TreeMixinPatternMatcher>> matchers;

        public MethodProxyBuilder() {
            this.matchers = new PriorityQueue<>();
        }

        public MethodProxyBuilder addMatcher(PriorityMatcher<TreeMixinPatternMatcher> matcher) {
            this.matchers.add(matcher);
            return this;
        }

        public MethodProxyBuilder addMatcher(TreeMixinPatternMatcher matcher, int priority) {
            this.matchers.add(new PriorityMatcher<>(priority, matcher));
            return this;
        }

        public MethodVisitor build(MethodVisitor parent) {
            return new MethodProxyAdapter(new ASMMethodProxy(this.matchers), parent);
        }
    }
}


