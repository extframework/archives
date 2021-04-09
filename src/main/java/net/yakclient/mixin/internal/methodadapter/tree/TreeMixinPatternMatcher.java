package net.yakclient.mixin.internal.methodadapter.tree;

import net.yakclient.mixin.internal.instruction.tree.TreeInstruction;
import net.yakclient.mixin.internal.methodadapter.MixinPatternMatcher;
import org.objectweb.asm.tree.AbstractInsnNode;

public abstract class TreeMixinPatternMatcher implements MixinPatternMatcher {
    private final TreeInstruction mixinInsn;

    public TreeMixinPatternMatcher(TreeInstruction instructions) {
        this.mixinInsn = instructions;
    }

    public void insertInsn(AbstractInsnNode index) {

//        new TreeInsnExecutor(this.mixinInsn, index).execute(this);
    }

//    @Override
//    public abstract void transform(InsnList instructions);
}
