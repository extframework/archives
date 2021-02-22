package net.yakclient.asm;

import net.yakclient.asm.classloader.CustomClassLoader;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class BasicASMTests {
    @Test
    public void testClassPrint() throws IOException {
        ClassVisitor printer = new ClassPrinter();
        ClassReader reader = new ClassReader("net.yakclient.asm.ASMTestCase");
        reader.accept(printer, 0);
    }

    /**
     * This will fail, not yet complete.
     *
     * @throws IOException In case of failure.
     */
    @Test
    public void testCreateClass() throws IOException {
        CustomClassLoader loader = new CustomClassLoader();
//        loader.loadSomeClass("net.yakclient.asm.ASMTestCase", "ASMTestCase");
    }

    /**
     * Fully works! This will remove all methods from a class given to it and output a class loaded
     * with the custom class loader.
     *
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    @Test
    public void testMethodRemover() throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        ASMTestCase test = new ASMTestCase("TestTHINGNIG");
        test.print();
//        this.testClassPrint();

        ClassWriter writer = new ClassWriter(0);

        ClassVisitor adapter = new RemoveMethodAdapter(writer);

        ClassReader reader = new ClassReader("net.yakclient.asm.ASMTestCase");
        reader.accept(adapter, 0);

        final byte[] b = writer.toByteArray();

        CustomClassLoader loader = new CustomClassLoader();
        loader.loadSomeClass(b, "net.yakclient.asm.ASMTestCase");

//        Object test2 = loader.loadClass("net.yakclient.asm.ASMTestCase").getConstructor(String.class).newInstance("SomethingCOOL");

        ClassWriter writer2 = new ClassWriter(0);

        ClassReader reader2 = new ClassReader("net.yakclient.asm.ASMTestCase");
        reader.accept(new ClassVisitor(Opcodes.ASM6, writer2) { }, 0);

        System.gc();

        loader.loadSomeClass(writer2.toByteArray(), "net.yakclient.asm.ASMTestCase");
    }


}
