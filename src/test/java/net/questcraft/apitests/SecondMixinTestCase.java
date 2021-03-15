package net.questcraft.apitests;

import net.yakclient.mixin.api.Injection;
import net.yakclient.mixin.api.InjectionType;
import net.yakclient.mixin.api.Mixer;
import org.objectweb.asm.Opcodes;

@Mixer("net.questcraft.apitests.MixinSourceClassTest")
public abstract class SecondMixinTestCase {
    @Injection(type = InjectionType.OVERWRITE)
    public void printTheString() {
        System.out.println("I also wanna print this");
    }
}
