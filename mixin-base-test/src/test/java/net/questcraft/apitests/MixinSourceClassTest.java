package net.questcraft.apitests;

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

    //var insn = this.getInsn(classTo, new MixinSource(new QualifiedMethodLocation(classTo, "printTheString(IF)V", InjectionType.AFTER_BEGIN, 2)));
    //
    //var nodes = new ArrayList<AbstractInsnNode>();
    //insn.getInstructions().forEach(nodes::add);
    //nodes


}
