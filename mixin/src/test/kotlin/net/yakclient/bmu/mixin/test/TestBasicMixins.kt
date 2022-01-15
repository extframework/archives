package net.yakclient.bmu.mixin.test

import net.yakclient.bmu.api.Bmu
import net.yakclient.bmu.api.Sources
import net.yakclient.bmu.api.mixin.Mixins
import net.yakclient.bmu.api.mixin.annotations.Injection
import net.yakclient.bmu.api.mixin.annotations.InjectionType
import net.yakclient.bmu.api.mixin.annotations.Mixer
import org.junit.jupiter.api.Test

class TestBasicMixins {

    private var value = "string of something"

    @Test
    fun `Test Source accumulation`() {
        println(Sources.sourceOf(TestBasicMixins::testMethod))
    }

    @Test
    fun `Test basic configuration creation`() {
        val mixinOf = Mixins.mixinOf(`Mixin test case`::class)
        println(mixinOf)
    }


    @Test
    fun `Test Bytecode modification with Mixins#apply`() {
        println( Class.forName("java.net.http.HttpRequest"))

        val config = Mixins.mixinOf(`Mixin test case`::class)

        val task = Bmu.apply<TestBasicMixins>(config)

        //Blocking
        task.join()

        testMethod()
    }

    fun testMethod(): String {
        println(value)
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

@Mixer("net.yakclient.bmu.mixin.test.TestBasicMixins")
abstract class `Mixin test case` {
    private var value = ""

    @Injection("testMethod()", type = InjectionType.AFTER_BEGIN)
    fun `Inject this!`() {
        value = "changed value"
    }
}