package net.yakclient.mixin.internal.methodadapter.tree;

import net.yakclient.mixin.internal.instruction.Instruction;
import net.yakclient.mixin.internal.instruction.tree.TreeInsnExecutor;
import net.yakclient.mixin.internal.instruction.ASMInstruction;
import net.yakclient.mixin.internal.methodadapter.MixinPatternMatcher;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

public abstract class TreeMixinPatternMatcher implements MixinPatternMatcher {
    private final Instruction mixinInsn;

    public TreeMixinPatternMatcher(Instruction instructions) {
        this.mixinInsn = instructions;
    }

    public void insertInsn(AbstractInsnNode index, InsnList list) {
        new TreeInsnExecutor(list, index, this.mixinInsn).execute();
    }


    public abstract void transform(InsnList insn);
}
