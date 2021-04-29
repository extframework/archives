package net.yakclient.mixin.base.internal.methodadapter.core;

import net.yakclient.mixin.base.YakMixins;
import net.yakclient.mixin.base.internal.bytecode.ByteCodeUtils;
import net.yakclient.mixin.base.internal.instruction.Instruction;
import net.yakclient.mixin.base.internal.instruction.core.CoreInsnExecutor;
import net.yakclient.mixin.base.internal.methodadapter.MixinPatternMatcher;
import org.objectweb.asm.MethodVisitor;

public abstract class CoreMixinPatternMatcher extends MethodVisitor implements MixinPatternMatcher {
    static final int NOT_MATCHED = 0;
    private final Instruction instructions;
    int state = NOT_MATCHED;
    private int locals = 1;

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

    @Override
    public void visitVarInsn(int opcode, int var) {
        if (this.mv instanceof CoreMixinPatternMatcher) super.visitVarInsn(opcode, var);
        else if (ByteCodeUtils.isLocalsStore(opcode) && var > this.locals) this.locals = var;
    }

    public abstract void visitInsn();
}
