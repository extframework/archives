package net.questcraft.apitests;


import net.yakclient.mixin.api.Injection;
import net.yakclient.mixin.api.InjectionType;
import net.yakclient.mixin.api.Mixer;

@Mixer("net.questcraft.apitests.MixinSourceClassTest")
public class MixinTestCase {
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

    @Injection(type = InjectionType.BEFORE_RETURN)
    public void printTheString(int test) {
        if (test > 10) System.out.println("THis");
    }
}
