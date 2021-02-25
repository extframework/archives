package net.yakclient.asm.method;

import net.yakclient.asm.classloader.CustomClassLoader;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class MethodByteCodeTester {

    @Test
    public void testByteCodeAdd() throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        ClassReader reader = new ClassReader("net.yakclient.asm.ASMTestCase");
        ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES);
        ClassVisitor visitor = new ASMClassAdapter(writer);

        reader.accept(visitor, 0);

        CustomClassLoader classLoader = new CustomClassLoader();
        classLoader.loadSomeClass(writer.toByteArray(), "net.yakclient.asm.ASMTestCase");
        final Object obj = classLoader.loadClass("net.yakclient.asm.ASMTestCase").getConstructor(String.class).newInstance("YAYA");
        obj.getClass().getMethod("print").invoke(obj);
    }
}
