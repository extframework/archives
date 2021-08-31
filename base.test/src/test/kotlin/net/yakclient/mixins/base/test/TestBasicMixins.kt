package net.yakclient.mixins.base.test

import net.yakclient.mixins.base.Injectors
import net.yakclient.mixins.base.Sources
import org.junit.jupiter.api.Test
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


    fun testMethod() : String {
        println("Hey, this is a ldc!")

        val integer = 5

        for (i in 0..integer) println("This is another ldc ;p")

        if (integer == 3) return "Hmm, looks like the integer is 3"

        return "All finished here"
    }
}