package net.yakclient.apitests;


import net.yakclient.mixin.internal.loader.PackageTarget;
import net.yakclient.mixin.registry.MixinRegistry;
import net.yakclient.mixin.registry.RegistryConfigurator;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;

//@RunWith(TestingProxyCSL.class)
public class RegistryTest {
    //for runtiem -Djava.system.class.loader=net/yakclient/mixin/internal/loader/ProxyClassLoader
    //for testing

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
//        System.out.println( ClassLoader.getSystemResourceAsStream(MixinSourceClassTest.class.getName().replace('.', '/') + ".class").available());

        final RegistryConfigurator configure = RegistryConfigurator.configure();
        final PackageTarget target = configure.addTarget(PackageTarget.create("net.questcraft"));
        final MixinRegistry mixinRegistry = configure.create();
        {
            mixinRegistry.registerMixin(net.questcraft.apitests.MixinTestCase.class).dumpAll();

            final Class<?> aClass = mixinRegistry.retrieveClass(net.questcraft.apitests.MixinSourceClassTest.class.getName()); //target.retrieveClass(MixinSourceClassTest.class.getName());
            final Object obj = aClass.getConstructor(String.class).newInstance("YAY");
//            aClass.getMethod("printTheString").invoke(obj);
        }

        {
            mixinRegistry.registerMixin(net.questcraft.apitests.SecondMixinTestCase.class, (cancel -> {
                System.out.println("Proxied");
                cancel.run();
            })).dumpAll();



            final Class<?> aClass = mixinRegistry.retrieveClass(net.questcraft.apitests.MixinSourceClassTest.class.getName());
            System.out.println("So we retrieved the class of " + aClass);

            System.out.println("And the constructors are " + args.getClass().getConstructors());

            final Constructor<?> constructor = aClass.getConstructor(String.class);
            System.out.println("Well we got a constructor");
            final Object obj = constructor.newInstance("YAY");
            System.out.println("And we made a instance of it");
            aClass.getMethod("printTheString").invoke(obj);
        }
//////
//        {
//            mixinRegistry.registerMixin(MixinTestCase.class).dumpAll();
//
//            final Class<?> bClass = target.get().retrieveClass(MixinSourceClassTest.class.getName());
//            final Object obj2 = bClass.getConstructor(String.class).newInstance("YAY");
//            bClass.getMethod("printTheString").invoke(obj2);
//        }
    }

    @Test
    public void testMixinRegistry() throws ExecutionException, InterruptedException, ClassNotFoundException {
        System.out.println("GOt here");
        RegistryConfigurator.configure().create().registerMixin(MixinTestCase.class).dumpAll();
        printSomething();
    }

//    @Test
//    public void byteCodeAdder() throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
//        final BytecodeMethodModifier methodModifier = new BytecodeMethodModifier();
//        byte[] b = methodModifier.combine(new QualifiedMethodLocation(MixinTestCase.class, "overrideIt", InjectionType.AFTER_BEGIN, Priority.MEDIUM), new MethodLocation(MixinSourceClassTest.class, "printTheString"));
////
////        ClassReader sourceReader = new ClassReader(b);
////        ClassVisitor visitor = new ClassVisitor(Opcodes.ASM9, new ClassWriter(ClassWriter.COMPUTE_FRAMES)) {
////            public MethodVisitor visitMethod(int access, String name,
////                                             String desc, String signature, String[] exceptions) {
////                MethodVisitor mv = cv.visitMethod(access, name, desc, signature,
////                        exceptions);
////
////                //TODO figure out the printer on textifier...
////                Printer p = new PrintTextifier();
////                mv = new TraceMethodVisitor(mv, p);
////                return mv;
////            }
////        };
////        sourceReader.accept(visitor, 0);
//
//        final Class<?> cls3 = new TargetClassLoader(this.getClass().getClassLoader(), ClassTarget.create(MixinSourceClassTest.class.getName())).defineClass(MixinSourceClassTest.class.getName(), b);
//
////        final Class<?> cls2 = new ClassLoader(this.getClass().getClassLoader()) {
////            public Class<?> defineClass(String name, byte[] b) throws ClassNotFoundException, IOException {
////                return this.defineClass(name, b, 0, b.length);
////            }
////        }.defineClass(MixinSourceClassTest.class.getName(), b);
//
//        final Object obj = cls3.getConstructor(String.class).newInstance("YAYA");
//        cls3.getMethod("printTheString").invoke(obj);
//
//        System.out.println(cls3);
//    }

    public void printSomething() {
        System.out.println("Something");
    }
}
