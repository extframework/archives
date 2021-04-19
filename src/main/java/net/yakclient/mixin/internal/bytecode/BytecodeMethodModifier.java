package net.yakclient.mixin.internal.bytecode;

import net.yakclient.mixin.internal.instruction.*;
import net.yakclient.mixin.internal.instruction.adapter.FieldSelfInsnAdapter;
import net.yakclient.mixin.internal.instruction.adapter.MethodSelfInsnAdapter;
import net.yakclient.mixin.internal.instruction.adapter.ReturnRemoverInsnAdapter;
import org.jetbrains.annotations.Contract;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class BytecodeMethodModifier {
    public <T extends Instruction> byte[] combine(String classTo, MixinDestination... destinations) throws IOException {
        final var instructions = new HashMap<String, Queue<QualifiedInstruction>>(destinations.length);

        if (destinations.length == 0) throw new IllegalArgumentException("Must provide destinations to mixin!");

        for (var destination : destinations) {
            final var current = new PriorityQueue<QualifiedInstruction>();

            for (MixinSource source : destination.getSources()) {
                Instruction insn = this.applyInsnAdapters(
                        this.getInsn(source.getLocation().getCls(), source),
                        classTo,
                        source.getLocation().getCls());

                current.add(new QualifiedInstruction(
                        source.getLocation().getPriority(),
                        source.getLocation().getInjectionType(),
                        insn));
            }
            instructions.put(destination.getMethod(), current);
        }

        return this.apply(instructions, classTo);
    }

    private Instruction getInsn(String cls, MixinSource source) throws IOException {
        final var sourceReader = new ClassReader(cls);

        final var cv = new InstructionClassVisitor(source instanceof MixinSource.MixinProxySource ?
                new ProxyASMInsnInterceptor(((MixinSource.MixinProxySource) source).getPointer()) :
                new ASMInsnInterceptor(), source.getLocation().getMethod());

        sourceReader.accept(cv, 0);
        return cv.getInstructions();
    }

    private Instruction applyInsnAdapters(Instruction instruction, String classTo, String classFrom) {
        final InsnAdapter returnAdapter = new ReturnRemoverInsnAdapter(),
                fieldAdapter = new FieldSelfInsnAdapter(returnAdapter, classTo, classFrom),
                methodAdapter = new MethodSelfInsnAdapter(fieldAdapter, classTo, classFrom);

        return methodAdapter.adapt(instruction);
    }


    @Contract(pure = true)
    private byte[] apply(Map<String, Queue<QualifiedInstruction>> injectors, String mixin) throws IOException {
        var writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        var adapter = new CoreMixinCV(writer, injectors);

        var reader = new ClassReader(mixin);
        reader.accept(adapter, 0);

        return writer.toByteArray();
    }

}
