package net.yakclient.mixin.internal.bytecode;

import net.yakclient.mixin.internal.YakMixinsInternal;
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

        final String qualifiedName = name + desc;
        if (this.hasInjection(qualifiedName)) {
            MethodVisitor last = visitor;

            final Queue<QualifiedInstruction> instructions = this.getInjection(qualifiedName);
            for (QualifiedInstruction insn : instructions) {
                last = YakMixinsInternal.corePatternMatcher(insn.getInjectionType(), last, insn.getInsn());
            }

            return last;
        }

        return visitor;
    }
}