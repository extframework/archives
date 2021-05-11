package net.yakclient.apitests

import net.questcraft.apitests.MixinSourceClassTest
import net.questcraft.apitests.MixinTestCase
import net.yakclient.mixin.base.registry.MixinRegistry
import net.yakclient.mixin.base.target.PackageTarget
import org.junit.jupiter.api.Test
import java.io.File

class RegistryTest {
    @Test
    fun testRegistryMixins() {
        val mixinRegistry = MixinRegistry()
        val context = mixinRegistry.applyTarget(PackageTarget.of("net.questcraft"))
        val lib = mixinRegistry.registerLib(
            File("${File(System.getProperty("user.dir")).parent}/lib/guava-17.0-TESTJAR.jar").toURI().toURL()
        )

        mixinRegistry.registerMixin(net.questcraft.apitests.SecondMixinTestCase::class.java).registerMixin(MixinTestCase::class.java).registerAll()

        val aClass = context.findClass(MixinSourceClassTest::class.java.name)
        val constructor = aClass.getConstructor(String::class.java)
        val obj = constructor.newInstance("YAY")
        aClass.getMethod("printTheString").invoke(obj)

        println(lib.findClass("com.google.common.math.DoubleMath").getMethod("isPowerOfTwo", Double::class.java).invoke(null, 5.0))
    }

}