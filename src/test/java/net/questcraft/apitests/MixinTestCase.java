package net.questcraft.apitests;

import net.yakclient.mixin.api.*;
import org.objectweb.asm.tree.InsnList;


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

    @Injection(to = "printTheString(I)V", type = InjectionType.BEFORE_END)
    public void overrideIt() {
        boolean boolVa = true;
        if (boolVa) System.out.println("THis");
    }
}
