package net.questcraft.apitests;

import net.yakclient.mixin.api.*;

//TODO THe VersionAdaptable just marks the annotation as not having "real" values, for the @Mixer, it means the value will actually just reference

@Mixer("net.questcraft.apitests.MixinSourceClassTest")
public  class MixinTestCase {
    private int shadowInt;
//    private String somethingElse;

//    @Shadow
//    public abstract void shadowMethod();

//    public abstract void second();

//    public static void staticTest() {
//        System.out.println("The current class is this");
//    }
/***
 *
 *  AFTER_BEGIN - WORKS
 *  BEFORE_END - FAILS
 *  BEFORE_RETURN - FAILS
 *  BEFORE_INVOKE - FAILS
 *  OVERWRITE - WORKS
 */


    @Injection(to = "printTheString(I)V", type = InjectionType.BEFORE_END)
    public void overrideIt(int integer) {
        boolean myTru = false;
//        if (myTru) System.out.println("THis");
        if (myTru) {
            String hello = "myStr";
        }
//        System.out.println("Other this");

    }
}
