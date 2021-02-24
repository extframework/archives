package net.yakclient.apitests;

import net.yakclient.mixin.api.Injection;
import net.yakclient.mixin.api.Mixer;

@Mixer(RegistryTest.class)
public abstract class MixinTestCase {

    @Injection(to = "printSomething")
    public void overrideIt() {
        System.out.println("No print this too!");
    }
}
