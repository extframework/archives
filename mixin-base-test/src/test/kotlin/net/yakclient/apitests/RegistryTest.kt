package net.yakclient.apitests

import net.questcraft.apitests.MixinSourceClassTest
import net.questcraft.apitests.MixinTestCase
import net.yakclient.mixin.api.Injection
import net.yakclient.mixin.api.Priority
import net.yakclient.mixin.base.registry.RegistryConfigurator
import org.junit.jupiter.api.Test
import java.lang.reflect.InvocationTargetException

class RegistryTest {
    @Test
    @Throws(ClassNotFoundException::class)
    fun testMixinRegistry() {
        println("GOt here")
        RegistryConfigurator.configure().create().registerMixin(MixinTestCase::class.java).dumpAll()
        printSomething()
    }

    fun printSomething() {
        println("Something")
    }


    @Test
    fun testRegistryMixins() {
        val configure =
            RegistryConfigurator.configure().addSafePackage("net.questcraft").addTarget("net.questcraft")
        val mixinRegistry = configure.create()
        mixinRegistry.registerMixin(MixinTestCase::class.java).dumpAll()

        mixinRegistry.registerMixin(net.questcraft.apitests.SecondMixinTestCase::class.java).dumpAll()
        val aClass = mixinRegistry.retrieveClass(MixinSourceClassTest::class.java.name)
        val constructor = aClass.getConstructor(String::class.java)
        val obj = constructor.newInstance("YAY")
        aClass.getMethod("printTheString").invoke(obj)
    }

}