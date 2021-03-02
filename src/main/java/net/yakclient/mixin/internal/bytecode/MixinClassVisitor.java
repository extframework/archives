package net.yakclient.mixin.internal.bytecode;

import net.yakclient.mixin.internal.instruction.Instruction;
import net.yakclient.mixin.internal.methodadapter.MethodInjectionPatternMatcher;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MixinClassVisitor extends ClassVisitor {
    private final Instruction instruction;
    private final MethodInjectionPatternMatcher.MatcherPattern pattern;
    private final String targetMethod;

    public MixinClassVisitor(ClassVisitor visitor, Instruction instruction, MethodInjectionPatternMatcher.MatcherPattern pattern, String targetMethod) {
        super(Opcodes.ASM6, visitor);
        this.instruction = instruction;
        this.pattern = pattern;
        this.targetMethod = targetMethod;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        final MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);

        if (name.equals(this.targetMethod) && visitor != null) return pattern.match(visitor, this.instruction);

        return visitor;
    }
}
