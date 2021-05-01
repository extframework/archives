package net.yakclient.mixin.base.internal.methodadapter.tree;

import net.yakclient.mixin.base.internal.instruction.Instruction;
import net.yakclient.mixin.base.internal.instruction.tree.InitialTreeInsnExecutor;
import net.yakclient.mixin.base.internal.instruction.tree.PlacedTreeInsnExecutor;
import net.yakclient.mixin.base.internal.methodadapter.MixinPatternMatcher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

public abstract class TreeMixinPatternMatcher implements MixinPatternMatcher {
    private final Instruction mixinInsn;

    public TreeMixinPatternMatcher(Instruction instructions) {
        this.mixinInsn = instructions;
    }

    public void insertInsn(@Nullable AbstractInsnNode index, @NotNull InsnList list) {
        if (index != null)
            new PlacedTreeInsnExecutor(list, index, this.mixinInsn).execute();
    }

    public void insertInsn(InsnList list) {
        new InitialTreeInsnExecutor(list, this.mixinInsn).execute();
    }

    public abstract void transform(InsnList insn);
}
