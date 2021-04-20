package net.yakclient.mixin.base.internal.instruction;

import org.objectweb.asm.tree.InsnList;

public interface Instruction {
    InsnList getInstructions();
}