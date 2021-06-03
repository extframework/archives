package net.yakclient.example

import net.yakclient.mixin.api.Injection
import net.yakclient.mixin.api.Mixer

@Mixer("net.yakclient.example.MixinSourceClassTest")
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
    @Injection
    fun printTheString() {
        val otherString = "clashing?"
        println(otherString)
    }
}