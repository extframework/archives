package net.yakclient.asm;

public class ASMTestCase {
    private final String testString;

    public ASMTestCase(String testString) {
        this.testString = testString;
    }

    public String getTestString() {
        return testString;
    }
    public void print() {
        System.out.println("testString");
    }

    public ASMTestCase create() {
        return new ASMTestCase("Testing??");
    }
}
