package net.yakclient.mixin.internal.methodadapter;

import net.yakclient.mixin.internal.bytecode.BytecodeMethodModifier;
import org.objectweb.asm.*;

import java.util.Queue;

public class OpcodeMatcherPattern extends MethodInjectionPatternMatcher {
    private final int opcode;

    public OpcodeMatcherPattern(MethodVisitor visitor, Queue<BytecodeMethodModifier.PriorityInstruction> instructions, PatternFlag<?>... flags) {
        super(visitor, instructions, flags);
        final Object flag = flags[0].getFlag();

        if (flags.length != 1) throw new IllegalArgumentException("Incorrect number of flags for OpcodePatternMatcher found");
        if (!(flag instanceof Integer)) throw new IllegalArgumentException("Wrong argument types for flag: " + flag);

        this.opcode = (Integer) flag;
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        if (this.opcode == opcode) this.executeInsn();
        super.visitVarInsn(opcode, var);
    }

    @Override
    public void visitTypeInsn(int opcode, String type) {
        if (this.opcode == opcode) this.executeInsn();
        super.visitTypeInsn(opcode, type);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        if (this.opcode == opcode) this.executeInsn();
        super.visitFieldInsn(opcode, owner, name, descriptor);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor) {
        if (this.opcode == opcode) this.executeInsn();
        super.visitMethodInsn(opcode, owner, name, descriptor);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        if (this.opcode == opcode) this.executeInsn();
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
        if (this.opcode == Opcodes.INVOKEDYNAMIC) this.executeInsn();
        super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
    }

    @Override
    public void visitJumpInsn(int opcode, Label label) {
        if (this.opcode == opcode) this.executeInsn();
        super.visitJumpInsn(opcode, label);
    }

    @Override
    public void visitLdcInsn(Object value) {
        if (this.opcode == Opcodes.LDC) this.executeInsn();
        super.visitLdcInsn(value);
    }

    @Override
    public void visitIincInsn(int var, int increment) {
        if (this.opcode == Opcodes.IINC) this.executeInsn();
        super.visitIincInsn(var, increment);
    }

    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        if (this.opcode == Opcodes.TABLESWITCH) this.executeInsn();
        super.visitTableSwitchInsn(min, max, dflt, labels);
    }

    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        if (this.opcode == Opcodes.LOOKUPSWITCH) this.executeInsn();
        super.visitLookupSwitchInsn(dflt, keys, labels);
    }

    @Override
    public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
        if (this.opcode == Opcodes.MULTIANEWARRAY) this.executeInsn();
        super.visitMultiANewArrayInsn(descriptor, numDimensions);
    }

    @Override
    public void visitInsn() {
    }
}
