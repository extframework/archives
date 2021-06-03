package net.yakclient.apitests

import net.yakclient.example.MixinSourceClassTest
import net.yakclient.example.MixinTestCase
import net.yakclient.example.SecondMixinTestCase
import net.yakclient.mixin.base.registry.MixinRegistry
import net.yakclient.mixin.base.target.PackageTarget
import org.junit.jupiter.api.Test
import java.io.File

class RegistryTest {
    @Test
    fun test() {
        val mixinRegistry = MixinRegistry()
        val context = mixinRegistry.applyTarget(PackageTarget.of("net.yakclient.example"))
        val lib = mixinRegistry.registerLib(RegistryTest::class.java.getResource("/lib/guava-17.0-TESTJAR.jar")!!)


        mixinRegistry.registerMixin(SecondMixinTestCase::class.java)
            .registerMixin(MixinTestCase::class.java).registerAll()

        val aClass = context.findClass(MixinSourceClassTest::class.java.name)
        val constructor = aClass.getConstructor(String::class.java)
        val obj = constructor.newInstance("YAY")
        aClass.getMethod("printTheString").invoke(obj)

        println(
            lib.findClass("com.google.common.math.DoubleMath").getMethod("isPowerOfTwo", Double::class.java)
                .invoke(null, 5.0)
        )
    }
}


