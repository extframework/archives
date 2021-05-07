package net.questcraft.apitests

class MixinSourceClassTest     //
//var nodes = new ArrayList<AbstractInsnNode>();
//insn.getInstructions().forEach(nodes::add);
//nodes
    (private val testString: String) {
    fun shadowMethod() {
        println("A shadow method has been called!")
    }

    fun printTheString() {
        println(this.testString)
    } //var insn = this.getInsn(classTo, new MixinSource(new QualifiedMethodLocation(classTo, "printTheString(IF)V", InjectionType.AFTER_BEGIN, 2)));
}