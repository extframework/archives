package net.questcraft.apitests;

public class MixinSourceClassTest {
    private final String testString;

    public MixinSourceClassTest(String testString) {
        this.testString = testString;
    }

    public void shadowMethod() {
        System.out.println("A shadow method has been called!");
    }


    public void printTheString(int integer) {
        System.out.println("ok");
//        if (integer < 10) System.out.println("THis");
//        System.out.println("Other this");
//        System.out.println(this.testString);
//        System.out.println(integer);
    }


}