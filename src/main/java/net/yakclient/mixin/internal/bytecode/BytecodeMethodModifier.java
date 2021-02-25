package net.yakclient.mixin.internal.bytecode;

import net.yakclient.mixin.internal.loader.ClassManager;
import net.yakclient.mixin.internal.loader.MixinClassLoader;
import net.yakclient.mixin.internal.methodadapter.MethodInjectionPatternMatcher;
import net.yakclient.mixin.registry.FunctionalProxy;
import net.yakclient.mixin.registry.pool.MethodLocation;
import net.yakclient.mixin.registry.pool.QualifiedMethodLocation;
import net.yakclient.mixin.registry.proxy.ProxiedPointer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;
import java.util.UUID;

public class BytecodeMethodModifier {
    public ClassLoader combine(QualifiedMethodLocation source, MethodLocation dest) throws IOException {

        ClassReader sourceReader = new ClassReader(source.getCls().getName());
        InstructionClassVisitor instructionVisitor = new InstructionClassVisitor(new ClassWriter(0), source.getMethod());
        sourceReader.accept(instructionVisitor, 0);

        return this.applyAndOverload(source, dest, instructionVisitor);
    }

    public ClassLoader combine(QualifiedMethodLocation source, MethodLocation dest, UUID pointer) throws IOException {
        ClassReader sourceReader = new ClassReader(source.getCls().getName());
        InstructionClassVisitor instructionVisitor = new InstructionClassVisitor.InstructionProxyVisitor(new ClassWriter(0), pointer, source.getMethod());
        sourceReader.accept(instructionVisitor, 0);

        return this.applyAndOverload(source, dest, instructionVisitor);
    }

    private ClassLoader applyAndOverload(QualifiedMethodLocation source, MethodLocation dest, InstructionClassVisitor instructionVisitor) throws IOException {
        if (!instructionVisitor.found())
            throw new IllegalArgumentException("Failed to find specified method through ASM");
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        ClassVisitor adapter = new MixinClassVisitor(writer, instructionVisitor.getInsn(), MethodInjectionPatternMatcher.MatcherPattern.pattern(source.getInjectionType()), dest.getMethod());

        ClassReader reader = new ClassReader(dest.getCls().getName());
        reader.accept(adapter, 0);

        final MixinClassLoader classloader = new MixinClassLoader();
        return ClassManager.applyOverload(classloader.defineClass(writer.toByteArray(), dest.getCls().getName()));
    }


}
