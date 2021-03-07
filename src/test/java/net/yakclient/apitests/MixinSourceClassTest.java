package net.yakclient.apitests;

public class MixinSourceClassTest {
    private final String testString;

    public MixinSourceClassTest(String testString) {
        this.testString = testString;
    }

    public void shadowMethod() {
        System.out.println("A shadow method has been called!");
    }

    public void printTheString() {
        System.out.println(this.testString);
    }


}
