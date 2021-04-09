package net.yakclient.mixin.internal.bytecode;

import net.yakclient.YakMixins;
import net.yakclient.mixin.internal.instruction.Instruction;
import net.yakclient.mixin.internal.instruction.core.InstructionPrinter;
import net.yakclient.mixin.internal.methodadapter.MixinPatternMatcher;
import net.yakclient.mixin.internal.methodadapter.core.CoreMixinPatternMatcher;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.ClassNode;

import java.util.Map;
import java.util.Queue;

public class MixinClassVisitor<T extends Instruction> extends ClassVisitor {
    private final Map<String, Map<Integer, Queue<PriorityInstruction<T>>>> injectors;

    public MixinClassVisitor(ClassVisitor visitor, Map<String, Map<Integer, Queue<PriorityInstruction<T>>>> injectors) {
        super(YakMixins.ASM_VERSION, visitor);
        this.injectors = injectors;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        final MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);

        final String qualifiedName = name + desc;
        if (this.hasInjection(qualifiedName)) {
            MethodVisitor last = new InstructionPrinter(visitor);
            new ClassNode(1, new ClassWriter(1));
            for (int type : this.getInjection(qualifiedName).keySet()) {
//                final CoreMixinPatternMatcher.MatcherPattern pattern = CoreMixinPatternMatcher.MatcherPattern.pattern(type);
                last = MixinPatternMatcher.pattern(type, last, this.getInjection(qualifiedName).get(type));
//                if (pattern ==)
//                    last = pattern.match(last, this.getInjection(qualifiedName).get(type), new CoreMixinPatternMatcher.PatternFlag<>(type));
//                else last = pattern.match(last, this.getInjection(qualifiedName).get(type));
            }

            return last;
        }

        return visitor;
    }

    private boolean hasInjection(String name) {
        for (String s : this.injectors.keySet()) {
            if (ByteCodeUtils.descriptorsSame(name, s)) return true;
        }
        return false;
    }

    private Map<Integer, Queue<PriorityInstruction<T>>> getInjection(String name) {
        for (String s : this.injectors.keySet()) {
            if (ByteCodeUtils.descriptorsSame(name, s)) return this.injectors.get(s);
        }
        throw new IllegalArgumentException("Failed to find injection");
    }

}