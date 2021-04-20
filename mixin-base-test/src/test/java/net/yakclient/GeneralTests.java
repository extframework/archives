package net.yakclient;

import net.yakclient.apitests.RegistryTest;
import net.yakclient.mixin.api.InjectionType;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeneralTests {

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
        String pattern = "[(].*[)]V";
        String line = "(asdfasdfasdfsadf)V";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(line);
        System.out.println(m.find());
    }

    @Test
    public void testMethodSignature() {
        String pattern = "^((?:[a-zA-Z0-9$_])+)" +
                "\\(((?:[ZCBSIFJD]|(?:L[a-zA-Z0-9$_.]+;)|(?:\\[[ZCBSIFJD])|(?:\\[L[a-zA-Z0-9$_.]+;))*)\\)" +
                "(?:[ZCBSIFJDV]|(?:L.+;)|(?:\\[[ZCBSIFJD]|(?:\\[L.+;)))";

        String line = "asdf([Lasdfasdf;[Ladsfadsf;)[B";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(line);
        System.out.println(m.find());
        System.out.println(m.groupCount());

        System.out.println(m.group(1)); //Name
        System.out.println(m.group(2)); //Params

    }

    @Test
    public void testMethodReturnSignature() {
        String pattern = "^(?:[ZCBSIFJDV]|(?:L[a-zA-Z0-9$_]+;)|(?:\\[[ZCBSIFJD])|(?:\\[L[a-zA-Z0-9$_]+;))$";
        String line = "[Lasdfasdf;";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(line);
        System.out.println(m.groupCount());

        System.out.println(m.find());
    }


    @Test
    public void testParamsSignature() {
        //This will produce a match here which is incorrect...
        String pattern = "^\\(((?:[ZCBSIFJD]|(?:L[a-zA-Z0-9$_.]+;)|(?:\\[[ZCBSIFJD])|(?:\\[L[a-zA-Z0-9$_.]+;))*)\\)$";
        String line = "(Ljava.lang.String;Ljava.lang.String;[Lasdfasdf;)";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(line);
        System.out.println(m.find());
        System.out.println(m.groupCount());

        System.out.println(m.group(1)); //This will be the signature for the params
    }

    @Test
    public void testParamsArraySignature() {
        //This will produce a match here which is incorrect...
        String pattern = "^\\(((?:(?:\\[[ZCBSIFJD])|(?:\\[L[a-zA-Z0-9$_]+;))*)\\)$";
        String line = "([B[Z[Lasdfasdf;[Lasdfasdf;)";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(line);
        System.out.println(m.find());
        System.out.println(m.groupCount());

//        System.out.println(m.group(1)); //
    }

    @Test
    public void testMethodParamsReturn() {
        String pattern = "\\(((?:[ZCBSIFJD]|(?:L.+;)|(?:\\[[ZCBSIFJD]|(?:\\[L.+;)))*)\\)" +
                "(?:[ZCBSIFJDV]|(?:L.+;)|(?:\\[[ZCBSIFJD]|(?:\\[L.+;)))";
        String line = "([Lasdfasdf;)V";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(line);
        System.out.println(m.find());
        System.out.println(m.groupCount());

        System.out.println(m.group(1));
//        System.out.println(m.group(2));

    }

    @Test
    public void testMethodName() {
        String pattern = "^((?:[a-zA-Z0-9$_])+)\\(";
        String line = "methodName1__$(";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(line);
        System.out.println(m.find());

        System.out.println(m.group(1)); //This will be the signature for the method name
    }


    public void test() {
        if (System.currentTimeMillis() > 10000) {
            System.out.println("");
        }
    }

    @Test
    public void testBitWiseOR() {
//        0b1   1   0  0 _1  0 0 1
//          256 128 64 32_16 8 2 1

//        0b1
        System.out.println(0b1);
        System.out.println(InjectionType.BEFORE_END << InjectionType.OVERWRITE);
    }
}
