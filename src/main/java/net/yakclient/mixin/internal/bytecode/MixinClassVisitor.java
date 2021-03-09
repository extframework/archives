package net.yakclient.mixin.internal.bytecode;

import net.yakclient.mixin.api.InjectionType;
import net.yakclient.mixin.internal.methodadapter.MethodInjectionPatternMatcher;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Map;
import java.util.Queue;

public class MixinClassVisitor extends ClassVisitor {
    private final Map<String, Map<InjectionType, Queue<BytecodeMethodModifier.PriorityInstruction>>> injectors;

    public MixinClassVisitor(ClassVisitor visitor, Map<String, Map<InjectionType, Queue<BytecodeMethodModifier.PriorityInstruction>>> injectors) {
        super(Opcodes.ASM9, visitor);
        this.injectors = injectors;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        final MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);
        if (this.injectors.containsKey(name) && visitor != null) {
            MethodVisitor last = visitor;

            for (InjectionType type : this.injectors.get(name).keySet()) {
                last = MethodInjectionPatternMatcher.MatcherPattern.pattern(type).match(last, this.injectors.get(name).get(type));
            }

            return last;
        }

        return visitor;
    }
}