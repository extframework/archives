package net.questcraft.apitests;

import net.yakclient.mixin.api.Injection;
import net.yakclient.mixin.api.Mixer;
import net.yakclient.mixin.api.Shadow;

//TODO THe VersionAdaptable just marks the annotation as not having "real" values, for the @Mixer, it means the value will actually just reference

@Mixer("net.questcraft.apitests.MixinSourceClassTest")
public abstract class MixinTestCase {
    private int shadowInt;
//    private String somethingElse;

    @Shadow
    public abstract void shadowMethod();

    public abstract void second();
    public static void staticTest() {
        System.out.println("The current class is this");
    }

    @Injection
    public void overrideIt() {
//        @NotNull String test = "THIS IS THE TEST STRING";
//        staticTest();
//        System.out.println("Print the test string " + test);
        System.out.println("No print this too!");
    }
}
