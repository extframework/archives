package net.yakclient.test.mixins

import net.yakclient.mixin.api.Injection
import net.yakclient.mixin.api.InjectionType
import net.yakclient.mixin.api.Mixer

@Mixer("net.yakclient.test.example.MixinSourceClassTest")
abstract class SecondMixinTestCase {
    @Injection//(type = InjectionType.BEFORE_RETURN)
    fun printTheString() {
        println("I also wanna print this")
    }
}