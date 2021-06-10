package net.yakclient.test.apitests

import net.yakclient.mixin.base.registry.ContextHandle
import net.yakclient.mixin.base.registry.MixinRegistry
import net.yakclient.test.mixins.MixinTestCase
import net.yakclient.test.mixins.SecondMixinTestCase
import org.junit.jupiter.api.Test

class RegistryTest {
    init {
        System.setProperty("yakclient.mixins.targets", "mixins.base.test.example")
    }
    @Test
    fun test() {
        val mixinRegistry = MixinRegistry()
        val lib = mixinRegistry.registerLib(RegistryTest::class.java.getResource("/lib/guava-17.0.jar")!!)

        val context: ContextHandle
        mixinRegistry
            .apply { registerMixin(SecondMixinTestCase::class.java) }
            .apply { context = registerMixin(MixinTestCase::class.java) }
            .apply { registerAll() }


        val aClass = context.findClass("net.yakclient.test.example.MixinSourceClassTest")
        val constructor = aClass.getConstructor(String::class.java)
        val obj = constructor.newInstance("YAY")
        aClass.getMethod("printTheString").invoke(obj)

        println(
            lib.findClass("com.google.common.math.DoubleMath").getMethod("isPowerOfTwo", Double::class.java)
                .invoke(null, 5.0)
        )
    }
}


