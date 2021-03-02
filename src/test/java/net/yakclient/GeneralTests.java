package net.yakclient;

import net.yakclient.apitests.MixinSourceClassTest;
import net.yakclient.apitests.RegistryTest;
import net.yakclient.mixin.internal.loader.PackageTarget;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneralTests {
    //
    @Test
    public void clsToPackageTest() {
       final Class<?> cls = MixinSourceClassTest.class;

        System.out.println(PackageTarget.create(cls.getPackage().getName()));
    }

    @Test
    public void reDefineCls() throws ClassNotFoundException, IOException {
        Class<?> cls1 = RegistryTest.class;

        final Class<?> cls2 = new ClassLoader(this.getClass().getClassLoader()) {
            public Class<?> defineClass(String name) throws ClassNotFoundException, IOException {
                final InputStream classData = this.getResourceAsStream(name.replace('.', '/') + ".class");
                if (classData == null) throw new ClassNotFoundException("Failed to find class " + name);

                final byte[] bytes = new byte[classData.available()];

                return this.defineClass(name, bytes, 0, classData.read(bytes));
            }
        }.defineClass(cls1.getName());

        System.out.println(cls1);
        System.out.println(cls2);
    }

    @Test
    public void testShouldReturn() {
        String pattern = "(.*)V";
        String line = "(asdfasdfasdfsadf)V";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(line);
        System.out.println(m.find());
    }
}
