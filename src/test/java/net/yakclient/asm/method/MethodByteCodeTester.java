package net.yakclient.asm.method;

import net.yakclient.apitests.MixinSourceClassTest;
import net.yakclient.apitests.SecondMixinTestCase;
import net.yakclient.asm.ASMTestCase;
import net.yakclient.asm.classloader.CustomClassLoader;
import net.yakclient.mixin.api.InjectionType;
import net.yakclient.mixin.internal.bytecode.BytecodeMethodModifier;
import net.yakclient.mixin.registry.pool.MethodLocation;
import net.yakclient.mixin.registry.pool.QualifiedMethodLocation;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class MethodByteCodeTester {

    @Test
    public void testByteCodeAdd() throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        final BytecodeMethodModifier modifier = new BytecodeMethodModifier();
        final byte[] b = modifier.combine(new MethodLocation(MixinSourceClassTest.class, "printTheString"),
                new BytecodeMethodModifier.ProxySource(new QualifiedMethodLocation(SecondMixinTestCase.class, "printTheString", InjectionType.AFTER_BEGIN, 2), new UUID(1,1)));

//        ClassReader sourceReader = new ClassReader(SecondMixinTestCase.class.getName());
//        InstructionClassVisitor instructionVisitor =
//                new InstructionClassVisitor.InstructionProxyVisitor(new ClassWriter( 0 ), "printTheString",SecondMixinTestCase.class.getName().replace(".", "/"), ASMTestCase.class.getName().replace(".", "/"), UUID.randomUUID());
//        sourceReader.accept(instructionVisitor, 0);

//      final byte[] b = BytecodeMethodModifier.apply(new HashMap<InjectionType, Queue<Instruction>>(){{
//            put(InjectionType.AFTER_BEGIN, new LinkedList<Instruction>(){{
//                add(instructionVisitor.getInsn());
//            }});
//        }}, new MethodLocation(ASMTestCase.class, "print"));

//        ClassReader reader = new ClassReader("net.yakclient.asm.ASMTestCase");
//        ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES);
//        ClassVisitor visitor = new MixinClassVisitor(writer, new HashMap<InjectionType, Queue<Instruction>>(){{
//            put(InjectionType.AFTER_BEGIN, new LinkedList<Instruction>(){{
//                add(instructionVisitor.getInsn());
//            }});
//        }}, "print");
//
//        reader.accept(visitor, 0);

        CustomClassLoader classLoader = new CustomClassLoader();
        Class<?> cls = classLoader.defineClass("net.yakclient.apitests.MixinSourceClassTest", b);
        final Object obj = cls.getConstructor(String.class).newInstance("YAYA");
        obj.getClass().getMethod("printTheString").invoke(obj);
    }
}
