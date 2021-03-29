package net.questcraft.apitests;

import net.yakclient.mixin.api.*;

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

    @Injection(to = "printTheString(I)V", type = InjectionType.BEFORE_END)
    public void overrideIt(int integer) {
        if (integer < 10) System.out.println("THis");
        System.out.println("Other this");
//        if (integer < 10) System.out.println("This");
//        System.out.println("Other this");
//        @NotNull String test = "THIS IS THE TEST STRING";
//        staticTest();
//        System.out.println("Print the test string " + test);
    }
}
