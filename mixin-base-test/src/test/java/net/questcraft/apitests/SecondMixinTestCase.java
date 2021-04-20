package net.questcraft.apitests;

import net.yakclient.mixin.api.Injection;
import net.yakclient.mixin.api.Mixer;

@Mixer("net.questcraft.apitests.MixinSourceClassTest")
public abstract class SecondMixinTestCase {
    @Injection
    public void printTheString(int integer) {
        System.out.println("I also wanna print this");
    }
}
