package net.yakclient.apitests;

import net.yakclient.mixin.api.InjectionType;
import net.yakclient.mixin.api.Priority;
import net.yakclient.mixin.internal.bytecode.BytecodeMethodModifier;
import net.yakclient.mixin.internal.loader.ClassTarget;
import net.yakclient.mixin.internal.loader.PackageTarget;
import net.yakclient.mixin.internal.loader.ProxyClassLoader;
import net.yakclient.mixin.internal.loader.TargetClassLoader;
import net.yakclient.mixin.registry.MixinRegistry;
import net.yakclient.mixin.registry.Pointer;
import net.yakclient.mixin.registry.RegistryConfigurator;
import net.yakclient.mixin.registry.pool.MethodLocation;
import net.yakclient.mixin.registry.pool.QualifiedMethodLocation;
import net.yakclient.mixin.registry.pool.SuppliedFuture;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.*;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

//@RunWith(TestingProxyCSL.class)
public class RegistryTest {
    //for runtiem -Djava.system.class.loader=net/yakclient/mixin/internal/loader/ProxyClassLoader
    //for testing

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        final RegistryConfigurator configure = RegistryConfigurator.configure();
        final SuppliedFuture<Pointer> target = configure.addTarget(PackageTarget.create("net.yakclient"));
        final MixinRegistry mixinRegistry = configure.create();

        mixinRegistry.registerMixin(MixinTestCase.class).dumpAll();

        final Class<?> aClass = target.get().retrieveClass(MixinSourceClassTest.class.getName());
        final Object obj = aClass.getConstructor(String.class).newInstance("YAYA");
        aClass.getMethod("printTheString").invoke(obj);
    }

    @Test
    public void testMixinRegistry() throws ExecutionException, InterruptedException {
        System.out.println("GOt here");
        RegistryConfigurator.configure().create().registerMixin(MixinTestCase.class).dumpAll();
        printSomething();
    }

    @Test
    public void byteCodeAdder() throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        final BytecodeMethodModifier methodModifier = new BytecodeMethodModifier();
        byte[] b = methodModifier.combine(new QualifiedMethodLocation(MixinTestCase.class, "overrideIt", InjectionType.AFTER_BEGIN, Priority.MEDIUM), new MethodLocation(MixinSourceClassTest.class, "printTheString"));
//
//        ClassReader sourceReader = new ClassReader(b);
//        ClassVisitor visitor = new ClassVisitor(Opcodes.ASM6, new ClassWriter(ClassWriter.COMPUTE_FRAMES)) {
//            public MethodVisitor visitMethod(int access, String name,
//                                             String desc, String signature, String[] exceptions) {
//                MethodVisitor mv = cv.visitMethod(access, name, desc, signature,
//                        exceptions);
//
//                //TODO figure out the printer on textifier...
//                Printer p = new PrintTextifier();
//                mv = new TraceMethodVisitor(mv, p);
//                return mv;
//            }
//        };
//        sourceReader.accept(visitor, 0);

        final Class<?> cls3 = new TargetClassLoader(this.getClass().getClassLoader(), ClassTarget.create(MixinSourceClassTest.class.getName())).defineClass(MixinSourceClassTest.class.getName(), b);

//        final Class<?> cls2 = new ClassLoader(this.getClass().getClassLoader()) {
//            public Class<?> defineClass(String name, byte[] b) throws ClassNotFoundException, IOException {
//                return this.defineClass(name, b, 0, b.length);
//            }
//        }.defineClass(MixinSourceClassTest.class.getName(), b);

        final Object obj = cls3.getConstructor(String.class).newInstance("YAYA");
        cls3.getMethod("printTheString").invoke(obj);

        System.out.println(cls3);
    }

    public void printSomething() {
        System.out.println("Something");
    }
}
