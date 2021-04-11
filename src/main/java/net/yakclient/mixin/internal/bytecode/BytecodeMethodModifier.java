package net.yakclient.mixin.internal.bytecode;

import net.yakclient.mixin.internal.instruction.Instruction;
import net.yakclient.mixin.internal.instruction.InstructionClassVisitor;
import net.yakclient.mixin.internal.instruction.ProxyASMInsnInterceptor;
import net.yakclient.mixin.internal.instruction.ASMInsnInterceptor;
import net.yakclient.mixin.registry.pool.QualifiedMethodLocation;
import org.jetbrains.annotations.Contract;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;
import java.util.*;

public class BytecodeMethodModifier {
    public <T extends Instruction> byte[] combine(String classTo, MixinDestination... destinations) throws IOException {
        final Map<String, Queue<QualifiedInstruction>> instructions = new HashMap<>(destinations.length);

        if (destinations.length == 0) throw new IllegalArgumentException("Must provide destinations to mixin!");

        for (MixinDestination destination : destinations) {
            final Queue<QualifiedInstruction> current = new PriorityQueue<>();

            for (MixinSource source : destination.getSources()) {
                current.add(new QualifiedInstruction(
                        source.getLocation().getPriority(),
                        source.getLocation().getInjectionType(),
                        this.getInsn(source.getLocation().getCls(), source)));
            }
            instructions.put(destination.getMethod(), current);
        }

//        for (MixinDestination destination : destinations) {
//            final String methodDest = destination.getMethod();
//
//            for (MixinSource source : destination.getSources()) {
//                final QualifiedMethodLocation location = source.getLocation();
//
//                final ClassReader sourceReader = new ClassReader(location.getCls());
//
//                final InstructionClassVisitor cv = new InstructionClassVisitor(source instanceof MixinSource.MixinProxySource ?
//                        new ProxyASMInsnInterceptor(((MixinSource.MixinProxySource) source).getPointer()) :
//                        new ASMInsnInterceptor(), source.getLocation().getMethod()); //, , location.getCls(),classTo
//
////                InstructionClassVisitor instructionVisitor = source instanceof MixinSource.MixinProxySource ?
////                        new InstructionClassVisitor.InstructionProxyVisitor(new ClassWriter(0), location.getMethod(), location.getCls(), classTo, ((MixinSource.MixinProxySource) source).getPointer()) :
////                        new InstructionClassVisitor(new ClassWriter(0), location.getMethod(), location.getCls(),classTo);
////                sourceReader.accept(instructionVisitor, 0);
//
//                instructions.putIfAbsent(methodDest, new HashMap<>());
//                if (!instructions.get(methodDest).containsKey(location.getInjectionType()))
//                    instructions.get(methodDest).put(location.getInjectionType(), new PriorityQueue<>());
//
//                instructions.get(methodDest).get(location.getInjectionType()).add(new QualifiedInstruction(source.getLocation().getPriority(), cv.getInstructions()));
//            }
//        }

        return this.apply(instructions, classTo);
    }

    private Instruction getInsn(String cls, MixinSource source) throws IOException {
        final ClassReader sourceReader = new ClassReader(cls);

        final InstructionClassVisitor cv = new InstructionClassVisitor(source instanceof MixinSource.MixinProxySource ?
                new ProxyASMInsnInterceptor(((MixinSource.MixinProxySource) source).getPointer()) :
                new ASMInsnInterceptor(), source.getLocation().getMethod());

        sourceReader.accept(cv, 0);
        return cv.getInstructions();
    }


    /*
       Map<Method name, Map<Injection location(4 values), Queue<Instructions with priority>>>, The target.
     */
    @Contract(pure = true)
    private byte[] apply(Map<String, Queue<QualifiedInstruction>> injectors, String mixin) throws IOException {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        ClassVisitor adapter = new CoreMixinCV(writer, injectors);

        ClassReader reader = new ClassReader(mixin);
        reader.accept(adapter, 0);

        return writer.toByteArray();
    }

}
