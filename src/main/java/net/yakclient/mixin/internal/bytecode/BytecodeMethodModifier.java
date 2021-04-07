package net.yakclient.mixin.internal.bytecode;

import net.yakclient.mixin.internal.instruction.Instruction;
import net.yakclient.mixin.registry.pool.QualifiedMethodLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;
import java.util.*;

public class BytecodeMethodModifier {
    //TODO figure out if compiled sources are really needed which i doubt they are.
    public byte[] combine(byte[] compiledSource, MixinDestination... destinations) throws IOException {
        final Map<String, Map<Integer, Queue<PriorityInstruction>>> instructions = new HashMap<>(destinations.length);

        if (!confirm(destinations)) throw new IllegalArgumentException("To combine all mixins must have the destination of the same class.");

        for (MixinDestination destination : destinations) {
            final String methodDest = destination.getMethod();

            for (MixinSource source : destination.getSources()) {
                final QualifiedMethodLocation location = source.getLocation();

                ClassReader sourceReader = new ClassReader(location.getCls());
                InstructionClassVisitor instructionVisitor = source instanceof MixinProxySource ?
                        new InstructionClassVisitor.InstructionProxyVisitor(new ClassWriter(0), location.getMethod(), location.getCls(), destination.getCls(), ((MixinProxySource) source).getPointer()) :
                        new InstructionClassVisitor(new ClassWriter(0), location.getMethod(), location.getCls(), destination.getCls());
                sourceReader.accept(instructionVisitor, 0);

                instructions.putIfAbsent(methodDest, new HashMap<>());
                if (!instructions.get(methodDest).containsKey(location.getInjectionType()))
                    instructions.get(methodDest).put(location.getInjectionType(), new PriorityQueue<>());

                instructions.get(methodDest).get(location.getInjectionType()).add(new PriorityInstruction(source.getLocation().getPriority(), instructionVisitor.getInsn()));
            }
        }

        return this.apply(instructions, compiledSource);
    }

    private boolean confirm(MixinDestination... destinations) {
        final String dest = destinations[0].getCls();
        for (MixinDestination destination : destinations) {
            if (!destination.getCls().equals(dest)) return false;
        }
        return true;
    }


    /*
       Map<Method name, Map<Injection location(4 values), Queue<Instructions with priority>>>, The target.
     */
    @Contract(pure = true)
    private byte[] apply(Map<String, Map<Integer, Queue<PriorityInstruction>>> injectors, byte[] sources) {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        ClassVisitor adapter = new MixinClassVisitor(writer, injectors);

        ClassReader reader = new ClassReader(sources);
        reader.accept(adapter, 0);

        final byte[] bytes = writer.toByteArray();
        return bytes;
    }

    public static class MixinDestination {
        private final String cls;
        private final String method;
        private final Set<MixinSource> sources;

        public MixinDestination(String cls, String method) {
            this.cls = cls;
            this.method = method;
            this.sources = new HashSet<>();
        }

        public MixinDestination addSource(MixinSource source) {
            this.sources.add(source);
            return this;
        }

        public String getCls() {
            return cls;
        }

        public String getMethod() {
            return method;
        }

        public MixinSource[] getSources() {
            return sources.toArray(new MixinSource[0]);
        }

        @Override
        public String toString() {
            return "MixinDestination{" +
                    "cls='" + cls + '\'' +
                    ", method='" + method + '\'' +
                    ", sources=" + sources +
                    '}';
        }
    }

    public static class PriorityInstruction implements Comparable<PriorityInstruction> {
        private final int priority;
        private final Instruction insn;

        public PriorityInstruction(int priority, Instruction insn) {
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

    public static class MixinSource {
        private final QualifiedMethodLocation location;

        public MixinSource(QualifiedMethodLocation location) {
            this.location = location;
        }

        public QualifiedMethodLocation getLocation() {
            return location;
        }

        @Override
        public String toString() {
            return "MixinSource{" +
                    "location=" + location +
                    '}';
        }
    }

    public static class MixinProxySource extends MixinSource {
        private final UUID pointer;

        public MixinProxySource(QualifiedMethodLocation location, UUID pointer) {
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
            MixinProxySource that = (MixinProxySource) o;
            return Objects.equals(pointer, that.pointer);
        }

        @Override
        public int hashCode() {
            return Objects.hash(pointer);
        }

        @Override
        public String toString() {
            return "MixinProxySource{" +
                    "pointer=" + pointer +
                    '}';
        }
    }
}
