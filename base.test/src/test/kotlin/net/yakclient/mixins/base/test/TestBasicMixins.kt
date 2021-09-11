package net.yakclient.mixins.base.test

import net.bytebuddy.agent.ByteBuddyAgent
import net.yakclient.mixins.api.Injection
import net.yakclient.mixins.api.InjectionType
import net.yakclient.mixins.api.Mixer
import net.yakclient.mixins.base.ClassResolver
import net.yakclient.mixins.base.Mixins
import net.yakclient.mixins.base.Sources
import net.yakclient.mixins.base.TransformerConfigurations
import net.yakclient.mixins.base.agent.YakMixinsAgent
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.tree.FieldNode
import java.lang.instrument.ClassDefinition

class TestBasicMixins {
    companion object {
        @JvmStatic
        private val somethingOrOther = "Hey"

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
    fun `Test basic configuration creation`() {
        val mixinOf = TransformerConfigurations.mixinOf(`Mixin test case`::class)
        println(mixinOf)
    }

    @Test
    fun `Test bytecode modification using ASM`() {

        val config = TransformerConfigurations.mixinOf(`Mixin test case`::class)
            .of {
                add { it: FieldNode ->
                    println(it.name)
                    println(it.value)
//                    it.value = "Not hey"
                    it
                }
            }.build()

        val classReader = ClassReader(TestBasicMixins::class.java.name)

        val writer = ClassWriter(ClassWriter.COMPUTE_FRAMES)

        val resolver = ClassResolver(writer, config)
        classReader.accept(resolver, 0)


        //why am i taking this for granted, god, instrumentation is amazing
        YakMixinsAgent.instrumentation.redefineClasses(
            ClassDefinition(
                TestBasicMixins::class.java,
                writer.toByteArray()
            )
        )
//        Mixins.apply(listOf( TestBasicMixins::class.java),  config)

        testMethod()

        println(somethingOrOther)
//        println(testMethod())
    }


    @Test
    fun `Test Bytecode modification with Mixins#apply`() {
        val config = TransformerConfigurations.mixinOf(`Mixin test case`::class)
            .of {
                add { it: FieldNode ->
                    println(it.name)
                    println(it.value)
                    it
                }
            }.build()

        val task = Mixins.apply<TestBasicMixins>(config)

        //Probably hasnt executed the task yet
//        testMethod()

        //Blocking
        task.join()

        testMethod()
//        `Inject this!`()
    }

    fun testMethod(): String {
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
        println("Somethien else")

        val integer = 5

        println(integer)

        if (integer == 5) println("Ok something else")

        for (i in 1..5) println("hey")

        println("Wait, this was the original, anything more?")
    }
}

@Mixer("something cool")
class `Mixin test case` {
    @Injection("testMethod()", type = InjectionType.BEFORE_INVOKE)
    fun `Inject this!`() {
        println("Get injected")

//        return "New return hahahh!"
    }
}