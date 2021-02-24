package net.yakclient.mixin.internal.methodadapter;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public abstract class MethodInjectionPatternMatcher extends MethodVisitor {
    public MethodInjectionPatternMatcher() {
        super(Opcodes.ASM6);
    }
}
