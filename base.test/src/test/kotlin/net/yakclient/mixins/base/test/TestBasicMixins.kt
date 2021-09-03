package net.yakclient.mixins.base.test

import net.yakclient.mixins.api.Injection
import net.yakclient.mixins.api.Mixer
import net.yakclient.mixins.base.*
import org.junit.jupiter.api.Test
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

class TestBasicMixins {
    @Test
    fun `Test Source accumulation`() {
        println(Sources.sourceOf(TestBasicMixins::testMethod))
    }

    @Test
    fun `Test injector matching`() {
        val source = Sources.sourceOf(TestBasicMixins::testMethod)

        println(Injectors.BEFORE_END.find(source))
        println(Injectors.AFTER_BEGIN.find(source))
        println(Injectors.BEFORE_INVOKE.find(source))
        println(Injectors.BEFORE_RETURN.find(source))
        println(Injectors.OPCODE_MATCHER.find(source, Opcodes.LDC))
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
    }


    fun testMethod(): String {
        println("Hey, this is a ldc!")

        val integer = 5

        for (i in 0..integer) println("This is another ldc ;p")

        if (integer == 3) return "Hmm, looks like the integer is 3"

        return "All finished here"
    }
}

@Mixer("something cool")
class `Mixin test case` {
    @Injection
    fun `Inject this!`() {
        println("Get injected")
    }
}