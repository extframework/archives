package net.yakclient.mixin.internal.instruction;

import org.objectweb.asm.tree.InsnList;

public interface Instruction {
    InsnList getInstructions();
}