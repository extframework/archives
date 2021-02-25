package net.yakclient.apitests;

import net.yakclient.TestingProxyCSL;
import net.yakclient.mixin.registry.RegistryConfigurator;
import org.junit.runner.RunWith;

import java.util.concurrent.ExecutionException;

@RunWith(TestingProxyCSL.class)
public class RegistryTest {
    //for runtiem -Djava.system.class.loader=net/yakclient/mixin/internal/loader/ProxyClassLoader
    //for testing


    public void testMixinRegistry() throws ExecutionException, InterruptedException {
        RegistryConfigurator.configure().create().registerMixin(MixinTestCase.class).dumpAll();
        this.printSomething();
    }

    public void printSomething() {
        System.out.println("Something");
    }
}
