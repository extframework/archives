package net.yakclient.mixins.base.test

import net.yakclient.mixins.api.Injection
import net.yakclient.mixins.api.InjectionType
import net.yakclient.mixins.api.Mixer
import net.yakclient.mixins.base.Mixins
import net.yakclient.mixins.base.Sources
import net.yakclient.mixins.base.TransformerConfigurations
import org.junit.jupiter.api.Test

class TestBasicMixins {

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
    fun `Test Bytecode modification with Mixins#apply`() {
        val config = TransformerConfigurations.mixinOf(`Mixin test case`::class).build()

        val task = Mixins.apply<TestBasicMixins>(config)

        //Blocking
        task.join()

        testMethod()
    }

    fun testMethod(): String {
        println("happens first")
        println("happens second")

        if (System.currentTimeMillis() > 0) println("hey")

        val integer = 5

        val o = listOf("Something", "somethign else")

        for (s in o) {
            println(s)
        }

        if (integer == 3) return "ITS 5!!!"

        for (i in 1..integer) {
            println("SOMETHING")
        }

        return "All finished here"
    }
}

@Mixer("something cool")
abstract class `Mixin test case` {
    @Injection("testMethod()", type = InjectionType.AFTER_BEGIN)
    fun `Inject this!`() {
        println("Get injected")
    }
}