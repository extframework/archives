package net.yakclient.mixin.internal.methodadapter.core;

import net.yakclient.YakMixins;
import net.yakclient.mixin.internal.instruction.Instruction;
import net.yakclient.mixin.internal.instruction.core.CoreInsnExecutor;
import net.yakclient.mixin.internal.methodadapter.MixinPatternMatcher;
import org.objectweb.asm.MethodVisitor;

public abstract class CoreMixinPatternMatcher extends MethodVisitor implements MixinPatternMatcher {
    static final int NOT_MATCHED = 0;
    private final Instruction instructions;
    int state = NOT_MATCHED;

    public CoreMixinPatternMatcher(MethodVisitor visitor, Instruction instructions) {
        super(YakMixins.ASM_VERSION, visitor);
        this.instructions = instructions;
    }


    void executeInsn() {
        this.executeInsn(this.instructions);
    }

    private void executeInsn(Instruction insn) {
        if (this.mv instanceof CoreMixinPatternMatcher) ((CoreMixinPatternMatcher) this.mv).executeInsn(insn);
        else new CoreInsnExecutor(insn, this.mv).execute();
    }

    @Override
    public void visitInsn(int opcode) {
        this.visitInsn();
        super.visitInsn(opcode);
    }

    @Override
    public void visitIntInsn(int opcode, int operand) {
        this.visitInsn();
        super.visitIntInsn(opcode, operand);
    }

    public abstract void visitInsn();
}
