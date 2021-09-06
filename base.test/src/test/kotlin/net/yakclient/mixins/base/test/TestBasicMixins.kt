package net.yakclient.mixins.base.test

import net.bytebuddy.agent.ByteBuddyAgent
import net.yakclient.mixins.api.Injection
import net.yakclient.mixins.api.InjectionType
import net.yakclient.mixins.api.Mixer
import net.yakclient.mixins.base.*
import net.yakclient.mixins.base.agent.YakMixinsAgent
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import java.lang.instrument.ClassDefinition

class TestBasicMixins {
    companion object {
        @BeforeAll
        @JvmStatic
        fun attachAgent() {
            YakMixinsAgent.instrumentation = ByteBuddyAgent.install()
        }
    }

    @Test
    fun `Test Source accumulation`() {
        println(Sources.sourceOf(TestBasicMixins::testMethod))
    }

    @Test
    fun `Test injector matching`() {
        val source = Sources.sourceOf(TestBasicMixins::testMethod)

        println(Injectors.BEFORE_END.find(source.get()))
        println(Injectors.AFTER_BEGIN.find(source.get()))
        println(Injectors.BEFORE_INVOKE.find(source.get()))
        println(Injectors.BEFORE_RETURN.find(source.get()))
        println(Injectors.OPCODE_MATCHER.find(source.get(), Opcodes.LDC))
    }

    @Test
    fun `Test basic configuration creation`() {
        val mixinOf = TransformerConfigurations.mixinOf(`Mixin test case`::class)
        println(mixinOf)
    }

    @Test
    fun `Test bytecode modification using ASM`() {

        val config = TransformerConfigurations.mixinOf(`Mixin test case`::class)

        val classReader = ClassReader(TestBasicMixins::class.java.name)

        val writer = ClassWriter(ClassWriter.COMPUTE_FRAMES)

        val resolver = ClassResolver(writer, config)
        classReader.accept(resolver, 0)


        //why am i taking this for granted, god, instrumentation is amazing
        YakMixinsAgent.instrumentation.redefineClasses(ClassDefinition(TestBasicMixins::class.java, writer.toByteArray()))
//        Mixins.apply(listOf( TestBasicMixins::class.java),  config)

        testMethod()
//        println(testMethod())
    }


    // FIXME: 9/5/21 There appear to be verify errors every once in a while with the on method invoke instructions, in general it appears to be iterating on a int stored as a local variable, though more testing is needed.
    fun testMethod() : String {
        println("happens first")
        println("happens second")

        if (System.currentTimeMillis() > 0) println("hey")
//        println("Hey, this is a ldc!")
//
        val integer = 5

        val o = listOf("Something", "somethign else")

        for (s in o) {
            println(s)
        }

        if (integer == 3) return "ITS 5!!!"

        for (i in 1..integer) {
            println("SOMETHING")
        }
//
//        if (integer == 3) return "Hmm, looks like the integer is 3"
//
        return "All finished here"
    }

    fun `Inject this!`() {
        println("Wait, this was the original, anything more?")
    }
}

@Mixer("something cool")
class `Mixin test case` {
    @Injection("testMethod()", type= InjectionType.BEFORE_END)
    fun `Inject this!`() {
        println("Get injected")
    }
}