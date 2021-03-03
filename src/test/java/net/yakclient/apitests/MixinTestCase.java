package net.yakclient.apitests;

import net.yakclient.mixin.api.Injection;
import net.yakclient.mixin.api.Mixer;
import net.yakclient.mixin.api.Shadow;

@Mixer(MixinSourceClassTest.class)
public abstract class MixinTestCase {

    @Shadow
    public abstract void shadowMethod();

    public abstract void second();


    @Injection(to = "printTheString")
    public void overrideIt() {
        this.shadowMethod();
        this.second();

        System.out.println("No print this too!");
    }
}
