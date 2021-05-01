package net.yakclient.apitests;


import net.questcraft.apitests.MixinTestCase;
import net.yakclient.mixin.base.registry.MixinRegistry;
import net.yakclient.mixin.base.registry.RegistryConfigurator;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class RegistryTest {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {


        final RegistryConfigurator configure = RegistryConfigurator.configure().addSafePackage("net.questcraft").addTarget("net.questcraft");
        final MixinRegistry mixinRegistry = configure.create();
        {
            mixinRegistry.registerMixin(MixinTestCase.class).dumpAll();

            final Class<?> aClass = mixinRegistry.retrieveClass(net.questcraft.apitests.MixinSourceClassTest.class.getName()); //target.retrieveClass(MixinSourceClassTest.class.getName());
//            final Constructor<?> constructor = aClass.getConstructor(String.class);
//            final Object obj = constructor.newInstance("YAY");
//            obj.getClass().getMethod("printTheString", int.class).invoke(obj,11);
//            new Label().toString();
        }

        {
            mixinRegistry.registerMixin(net.questcraft.apitests.SecondMixinTestCase.class).dumpAll();


            final Class<?> aClass = mixinRegistry.retrieveClass(net.questcraft.apitests.MixinSourceClassTest.class.getName());

            final Constructor<?> constructor = aClass.getConstructor(String.class);
            final Object obj = constructor.newInstance("YAY");
            aClass.getMethod("printTheString").invoke(obj);
        }

    }

    @Test
    public void testMixinRegistry() throws ClassNotFoundException {
        System.out.println("GOt here");
        RegistryConfigurator.configure().create().registerMixin(MixinTestCase.class).dumpAll();
        printSomething();
    }


    public void printSomething() {
        System.out.println("Something");
    }
}
