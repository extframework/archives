package net.yakclient.mixin.internal.bytecode;

import net.yakclient.mixin.api.InjectionType;
import net.yakclient.mixin.internal.instruction.Instruction;
import net.yakclient.mixin.registry.pool.Location;
import net.yakclient.mixin.registry.pool.MethodLocation;
import net.yakclient.mixin.registry.pool.QualifiedMethodLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;
import java.util.*;

public class BytecodeMethodModifier {
    public byte[] combine(MethodLocation dest, Source... sources) throws IOException {
        final Map<InjectionType, Queue<PriorityInstruction>> instructions = new HashMap<>();

        for (Source source : sources) {
            final QualifiedMethodLocation location = source.getLocation();

            ClassReader sourceReader = new ClassReader(location.getCls().getName());
            InstructionClassVisitor instructionVisitor = source instanceof ProxySource ?
                    new InstructionClassVisitor.InstructionProxyVisitor(new ClassWriter( 0 ), location.getMethod(), location.getCls().getName(), dest.getCls().getName(), ((ProxySource) source).getPointer()) :
                    new InstructionClassVisitor(new ClassWriter(0), location.getMethod(), location.getCls().getName(), dest.getCls().getName());
            sourceReader.accept(instructionVisitor, 0);

            if (!instructions.containsKey(location.getInjectionType()))
                instructions.put(location.getInjectionType(), new PriorityQueue<>());

            instructions.get(location.getInjectionType()).add(new PriorityInstruction(source.getLocation().getPriority(), instructionVisitor.getInsn()));
        }

        return this.apply(instructions, dest);
    }


//    public byte[] combine(ProxySource[] sources, MethodLocation dest) throws IOException {
//        final Map<InjectionType, Queue<Instruction>> instructions = new HashMap<>();
//
//        for (ProxySource source : sources) {
//
//            final QualifiedMethodLocation location = source.getSource().getLocation();
//
//
//            ClassReader sourceReader = new ClassReader(location.getCls().getName());
//            InstructionClassVisitor instructionVisitor = new InstructionClassVisitor.InstructionProxyVisitor(new ClassWriter(0), location.getMethod(), location.getCls().getName(), dest.getCls().getName(), source.getPointer());
//            sourceReader.accept(instructionVisitor, 0);
//
//            if (!instructions.containsKey(location.getInjectionType()))
//                instructions.put(location.getInjectionType(), new LinkedList<>());
//
//            instructions.get(location.getInjectionType()).add(instructionVisitor.getInsn());
//        }
//
//        return this.apply(instructions, dest);
//    }

    @Contract(pure = true)
    private byte[] apply(Map<InjectionType, Queue<PriorityInstruction>> injectors, MethodLocation dest) throws IOException {
//        if (!instructionVisitor.found())
//            throw new IllegalArgumentException("Failed to find specified method through ASM");
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

//        ClassVisitor adapter = new MixinClassVisitor(writer, instructionVisitor.getInsn(), MethodInjectionPatternMatcher.MatcherPattern.pattern(source.getInjectionType()), dest.getMethod());
        ClassVisitor adapter = new MixinClassVisitor(writer, injectors, dest.getMethod());

        ClassReader reader = new ClassReader(dest.getCls().getName());
        reader.accept(adapter, 0);

        return writer.toByteArray();
//        final MixinClassLoader classloader = new MixinClassLoader(ClassManager.parentLoader());
//        return ClassManager.applyOverload(classloader.defineClass(writer.toByteArray(), dest.getCls().getName()));
    }

    public static class PriorityInstruction implements Comparable<PriorityInstruction> {
        private final int priority;
        private final Instruction insn;

        PriorityInstruction(int priority, Instruction insn) {
            this.priority = priority;
            this.insn = insn;
        }

        @Override
        public int compareTo(@NotNull BytecodeMethodModifier.PriorityInstruction o) {
            return o.priority - this.priority;
        }

        public Instruction getInsn() {
            return this.insn;
        }
    }

    public static class Source {
        private final QualifiedMethodLocation location;

        public Source(QualifiedMethodLocation location) {
            this.location = location;
        }

        public QualifiedMethodLocation getLocation() {
            return location;
        }
    }

    public static class ProxySource extends Source {
        private final UUID pointer;

        public ProxySource(QualifiedMethodLocation location, UUID pointer) {
            super(location);
            this.pointer = pointer;
        }

        public UUID getPointer() {
            return pointer;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ProxySource that = (ProxySource) o;
            return Objects.equals(pointer, that.pointer);
        }

        @Override
        public int hashCode() {
            return Objects.hash(pointer);
        }
    }

//    public static class ByteCodeInjector {
//        private final InjectionType type;
//        private final Instruction insn;
//        private final int priority;
//
//        ByteCodeInjector(InjectionType type, Instruction insn, int priority) {
//            this.type = type;
//            this.insn = insn;
//            this.priority = priority;
//        }
//
//        public InjectionType getType() {
//            return type;
//        }
//
//        public Instruction getInsn() {
//            return insn;
//        }
//
//        public int getPriority() {
//            return priority;
//        }
//    }


}
