package net.yakclient.mixin.internal.bytecode;

import net.yakclient.mixin.internal.methodadapter.MethodInjectionPatternMatcher;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Map;
import java.util.Queue;

import static net.yakclient.mixin.internal.methodadapter.MethodInjectionPatternMatcher.MatcherPattern.MATCH_OPCODE;

public class MixinClassVisitor extends ClassVisitor {
    private final Map<String, Map<Integer, Queue<BytecodeMethodModifier.PriorityInstruction>>> injectors;

    public MixinClassVisitor(ClassVisitor visitor, Map<String, Map<Integer, Queue<BytecodeMethodModifier.PriorityInstruction>>> injectors) {
        super(Opcodes.ASM9, visitor);
        this.injectors = injectors;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        final MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);

        final String qualifiedName = name + desc;
        if (this.hasInjection(qualifiedName)) {
            MethodVisitor last = visitor;

            for (int type : this.getInjection(qualifiedName).keySet()) {
                final MethodInjectionPatternMatcher.MatcherPattern pattern = MethodInjectionPatternMatcher.MatcherPattern.pattern(type);
                if (pattern == MATCH_OPCODE)
                    last = pattern.match(last, this.getInjection(qualifiedName).get(type), new MethodInjectionPatternMatcher.PatternFlag<>(type));
                else last = pattern.match(last, this.getInjection(qualifiedName).get(type));
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

    private Map<Integer, Queue<BytecodeMethodModifier.PriorityInstruction>> getInjection(String name) {
        for (String s : this.injectors.keySet()) {
            if (ByteCodeUtils.descriptorsSame(name, s)) return this.injectors.get(s);
        }
        throw new IllegalArgumentException("Failed to find injection");
    }

}