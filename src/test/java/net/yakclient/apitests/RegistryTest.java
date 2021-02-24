package net.yakclient.apitests;

import net.yakclient.mixin.registry.RegistryConfigurator;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

public class RegistryTest {
    @Test
    public void testMixinRegistry() throws ExecutionException, InterruptedException {
        RegistryConfigurator.configure().create().registerMixin(MixinTestCase.class);
        this.printSomething();
    }

    public void printSomething() {
        System.out.println("Something");
    }
}
