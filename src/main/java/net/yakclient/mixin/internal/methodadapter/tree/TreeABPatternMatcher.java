package net.yakclient.mixin.internal.methodadapter.tree;

import net.yakclient.YakMixins;
import net.yakclient.mixin.internal.bytecode.PriorityInstruction;
import net.yakclient.mixin.internal.instruction.core.CoreInstruction;
import net.yakclient.mixin.internal.instruction.tree.TreeInstruction;
import org.objectweb.asm.tree.InsnList;

import java.util.Queue;

public class TreeABPatternMatcher extends TreeMixinPatternMatcher {
    public TreeABPatternMatcher(TreeInstruction instructions) {
        super(YakMixins.ASM_VERSION, instructions);
    }


    @Override
    public void transform(InsnList instructions) {

    }
}
