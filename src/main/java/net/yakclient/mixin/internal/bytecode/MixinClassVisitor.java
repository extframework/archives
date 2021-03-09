package net.yakclient.mixin.internal.bytecode;

import net.yakclient.mixin.api.InjectionType;
import net.yakclient.mixin.internal.instruction.Instruction;
import net.yakclient.mixin.internal.methodadapter.MethodInjectionPatternMatcher;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Comparator;
import java.util.Map;
import java.util.Queue;

public class MixinClassVisitor extends ClassVisitor {
    private final Map<InjectionType, Queue<BytecodeMethodModifier.PriorityInstruction>> injectors;
    //    private final Instruction[] instructions;
//    private final MethodInjectionPatternMatcher.MatcherPattern pattern;
    private final String targetMethod;

    public MixinClassVisitor(ClassVisitor visitor, Map<InjectionType, Queue<BytecodeMethodModifier.PriorityInstruction>> injectors, String targetMethod) {
        super(Opcodes.ASM6, visitor);
        this.injectors = injectors;
        this.targetMethod = targetMethod;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        final MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);
        if (name.equals(this.targetMethod) && visitor != null) {
            MethodVisitor last = visitor;

            for (InjectionType type : this.injectors.keySet()) {
                last = MethodInjectionPatternMatcher.MatcherPattern.pattern(type).match(last, this.injectors.get(type));
            }

            return last;
        }
//        if (name.equals(this.targetMethod) && visitor != null) return pattern.match(visitor, this.injectors);

        return visitor;
    }
}