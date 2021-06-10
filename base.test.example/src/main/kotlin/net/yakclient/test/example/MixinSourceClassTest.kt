package net.yakclient.test.example

class MixinSourceClassTest
    (private val testString: String) {
    fun shadowMethod() {
        println("A shadow method has been called!")
    }

    fun printTheString() {
        println(this.testString)
    }
}