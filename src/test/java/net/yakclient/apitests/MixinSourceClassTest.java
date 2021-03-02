package net.yakclient.apitests;

public class MixinSourceClassTest {
    private final String testString;

    public MixinSourceClassTest(String testString) {
        this.testString = testString;
    }

    public void printTheString() {
        System.out.println("I love combining bytecode");
    }
}
