package net.yakclient.apitests;

import net.yakclient.mixin.registry.RegistryConfigurator;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;

//@RunWith(TestingProxyCSL.class)
public class RegistryTest {
    //for runtiem -Djava.system.class.loader=net/yakclient/mixin/internal/loader/ProxyClassLoader
    //for testing

    public static void main(String[] args) throws ClassNotFoundException {
        System.out.println("GOt here");
        final Class<RegistryConfigurator> registryConfiguratorClass = RegistryConfigurator.class;
        RegistryConfigurator.configure().create().registerMixin(MixinTestCase.class).dumpAll();
//        Thread.currentThread().getContextClassLoader().loadClass(MixinTestCase.class.getName());
        Class<?> cls = MixinSourceClassTest.class;
        MixinSourceClassTest test = new MixinSourceClassTest("Ayay, whats this:?");
        test.getClass().getClassLoader();
        System.out.println("AYAYA");
    }

    @Test
    public void testMixinRegistry() throws ExecutionException, InterruptedException {
        System.out.println("GOt here");
        RegistryConfigurator.configure().create().registerMixin(MixinTestCase.class).dumpAll();
        printSomething();
    }

    public void printSomething() {
        System.out.println("Something");
    }
}
