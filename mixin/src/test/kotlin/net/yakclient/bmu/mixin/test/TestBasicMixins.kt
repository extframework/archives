package net.yakclient.bmu.mixin.test

import net.yakclient.bmu.api.Archives
import net.yakclient.bmu.api.transform.Sources
import net.yakclient.bmu.api.mixin.Mixins
import net.yakclient.bmu.api.transform.TransformerConfig
import org.junit.jupiter.api.Test
import org.objectweb.asm.ClassReader

class TestBasicMixins {
    @Test
    fun `Test Source accumulation`() {
        println(Sources.sourceOf(MixedClass::testMethod))
    }

    private fun getConfig(): TransformerConfig {
       return Mixins.mixinOf(
            "net.yakclient.bmu.mixin.test.MixedClass",
            `Mixin test case`::class.java.name,
            listOf(Mixins.InjectionMetaData(Sources.sourceOf(`Mixin test case`::`Inject this!`), to = "testMethod()V"))
        )
    }

    @Test
    fun `Test basic configuration creation`() {
        val mixinOf = getConfig()
        println(mixinOf)
    }


    @Test
    fun `Test Bytecode modification with Mixins#resolve`() {
        println(Class.forName("java.net.http.HttpRequest"))

        val config = getConfig()

        val bytes = Archives.resolve(ClassReader(MixedClass::class.java.name), config)

        val loader = object : ClassLoader() {
            fun defineClass(name: String, bytes: ByteArray) = super.defineClass(name, bytes, 0, bytes.size)
        }

        val c = loader.defineClass("net.yakclient.bmu.mixin.test.MixedClass", bytes)
        val instance = c.getConstructor().newInstance()
        c.getMethod("testMethod").invoke(instance)
    }
}

class MixedClass {
    private var value = "string of something"


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

abstract class `Mixin test case` {
    private var value = ""

    fun `Inject this!`() {
        value = "changed value"
    }
}