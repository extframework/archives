package net.yakclient.mixin.internal.bytecode;

import net.yakclient.mixin.registry.FunctionalProxy;
import net.yakclient.mixin.registry.pool.MethodLocation;
import net.yakclient.mixin.registry.pool.QualifiedMethodLocation;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

import java.io.File;
import java.io.IOException;

public class BytecodeMethodModifier {
//    public BytecodeMethodModifier combine(QualifiedMethodLocation source, MethodLocation dest) throws IOException {
//        final File f = new File((source.getCls().getProtectionDomain().getCodeSource().getLocation().getPath());
//
//        ClassReader reader = new ClassReader(f.getPath());
//        ByteCodeClassVisitor visitor = new ByteCodeClassVisitor();
//        reader.accept(visitor, 0);
//
//        visitor.
//
//        CustomClassLoader classLoader = new CustomClassLoader();
//        classLoader.loadSomeClass(writer.toByteArray(), "net.yakclient.asm.ASMTestCase");
//        final Object obj = classLoader.loadClass("net.yakclient.asm.ASMTestCase").getConstructor(String.class).newInstance("YAYA");
//        obj.getClass().getMethod("print").invoke(obj);
//    }
//
//    public BytecodeMethodModifier combine(QualifiedMethodLocation source, MethodLocation dest, FunctionalProxy proxy) {
//
//    }



}
