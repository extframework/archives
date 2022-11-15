package net.yakclient.archives.mixin.test

import net.yakclient.archives.mixin.*
import net.yakclient.archives.transform.Sources
import net.yakclient.archives.transform.TransformerConfig.Companion.plus
import kotlin.test.Test

class TestMethodMixins {
    @Test
    fun `Basic method injection`() {
        val config = MethodInjection.apply(
            MethodInjectionData(
                MixTo::class.java.name,
                MethodSource::class.java.name,
                Sources.of(MethodSource::injectThisMethod),

                name = "injectThisMethod",
                desc = "()V"
            )
        )

        val c = MixTo::class.java.transform(config)
        val instance = c.noArgInstance()

        c.getMethod("injectThisMethod").invoke(instance)
    }

    @Test
    fun `Test method injection with differing source names`() {
        val config = MethodInjection.apply(
            MethodInjectionData(
                MixTo::class.java.name,
                MethodSource::class.java.name,
                Sources.of(MethodSource::injectThisMethod),

                name = "newMethodName???",
                desc = "()V"
            )
        )

        val c = MixTo::class.java.transform(config)
        val instance = c.noArgInstance()

        c.getMethod("newMethodName???").invoke(instance)
    }

    @Test
    fun `Test method injection with source injection`() {
        val methodConfig = MethodInjection.apply(
            MethodInjectionData(
                MixTo::class.java.name,
                MethodSource::class.java.name,
                Sources.of(MethodSource::injectThisMethod),

                name = "injectThisMethod",
                desc = "()V"
            )
        )

        val sourceConfig = SourceInjection.apply(
            SourceInjectionData(
                MixTo::class.java.name,
                MethodSource::class.java.name,
                listOf(
                    SourceInjectionData.From(
                        Sources.of(MethodSource::mixThis),
                        "thisMethodIsAlreadyHere()V",
                        SourceInjectors.AFTER_BEGIN
                    )
                )
            )
        )

        val config = methodConfig + sourceConfig

        val c = MixTo::class.java.transform(config)
        val instance = c.noArgInstance()

        c.getMethod("thisMethodIsAlreadyHere").invoke(instance)
    }
}

class MixTo {
    fun thisMethodIsAlreadyHere() {
        println("This method was already here")
    }
}

private class MethodSource {
    fun injectThisMethod() {
        println("This method was injected")
    }

    fun mixThis() {
        injectThisMethod()

        println("Mixed")
    }
}