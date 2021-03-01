package net.yakclient.apitests;

import net.yakclient.mixin.api.Injection;
import net.yakclient.mixin.api.Mixer;

@Mixer(MixinSourceClassTest.class)
public abstract class MixinTestCase {

    @Injection(to = "printTheString")
    public void overrideIt() {
        System.out.println("No print this too!");
    }
}
