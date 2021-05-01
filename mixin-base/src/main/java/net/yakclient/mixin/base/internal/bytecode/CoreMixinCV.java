package net.yakclient.mixin.base.internal.bytecode;

import net.yakclient.mixin.base.internal.methodadapter.MixinPatternMatcher;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.Map;
import java.util.Queue;

public class CoreMixinCV extends MixinCV {
    public CoreMixinCV(ClassVisitor visitor, Map<String, Queue<QualifiedInstruction>> injectors) {
        super(visitor, injectors);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        final MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);

        final var qualifiedName = name + desc;
        if (this.hasInjection(qualifiedName)) {
            var last = visitor;

            final Queue<QualifiedInstruction> instructions = this.getInjection(qualifiedName);
            for (QualifiedInstruction insn : instructions) {
                last = MixinPatternMatcher.corePatternMatcher(insn.getInjectionType(), last, insn.getInsn());
            }

            return last;
        }

        return visitor;
    }
}