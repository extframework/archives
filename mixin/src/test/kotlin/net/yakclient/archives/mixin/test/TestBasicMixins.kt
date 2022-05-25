package net.yakclient.archives.mixin.test

import net.yakclient.archives.Archives
import net.yakclient.archives.mixin.InjectionType
import net.yakclient.archives.mixin.Mixins
import net.yakclient.archives.transform.ByteCodeUtils
import net.yakclient.archives.transform.Sources
import net.yakclient.archives.transform.TransformerConfig
import org.junit.jupiter.api.Test
import org.objectweb.asm.ClassReader
import kotlin.math.floor

class TestBasicMixins {
    @Test
    fun `Test Source accumulation`() {
        println(Sources.of(MixedClass::testMethod))
    }

    private fun getConfig(): TransformerConfig {
        return Mixins.mixinOf(
            MixedClass::class.java.name,
            `Mixin test case`::class.java.name,
            listOf(
                Mixins.InjectionMetaData(
                    Sources.of(`Mixin test case`::`Inject this!`),
                    ByteCodeUtils.runtimeSignature(MixedClass::testMethod),
                    InjectionType.AFTER_BEGIN
                )
            )
        )
    }

    @Test
    fun `Test basic configuration creation`() {
        val mixinOf = getConfig()
        println(mixinOf)
    }


    @Test
    fun `Test Bytecode modification with Mixins#resolve`() {
        val config = getConfig()

        val bytes = Archives.resolve(ClassReader(MixedClass::class.java.name), config)

        val loader = object : ClassLoader() {
            fun defineClass(name: String, bytes: ByteArray) = super.defineClass(name, bytes, 0, bytes.size)
        }

        val c = loader.defineClass(MixedClass::class.java.name, bytes)
        val instance = c.getConstructor().newInstance()
        println(c.getMethod("testMethod", Int::class.java).invoke(instance, 1))
    }
}

class MixedClass {
    private var value = "string of something"

    fun testMethod(first: Int): String {
        println(value)
        println("happens second")

        println(first)

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
        var a = ""
//
        repeat( floor(Math.random() * 10).toInt()) {
            a += "a"
        }

//       println(a)
    }
}