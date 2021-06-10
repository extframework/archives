package net.yakclient.test.mixins

import net.yakclient.mixin.api.Injection
import net.yakclient.mixin.api.InjectionType
import net.yakclient.mixin.api.Mixer
import net.yakclient.mixin.api.Priority

@Mixer("net.yakclient.test.example.MixinSourceClassTest", module = "mixins.base.test.example")
class MixinTestCase {
    //    private int shadowInt;
    //    private String somethingElse;
    //    @Shadow
    //    public abstract void shadowMethod();
    //
    //    public abstract void second();
    //
    //    public static void staticTest() {
    //        System.out.println("The current class is this");
    //    }
    @Injection(priority = Priority.HIGH)
    fun printTheString() {
        val otherString = "clashing?"
        println(otherString)
    }
}