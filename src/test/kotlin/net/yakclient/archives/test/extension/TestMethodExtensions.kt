package net.yakclient.archives.test.extension

import net.yakclient.archives.extension.Method
import org.objectweb.asm.Type
import kotlin.test.Test

class TestMethodExtensions {
    fun checkType(type1: Type, type2: Type): Boolean {
        return type1.sort == type2.sort && type1.internalName == type2.internalName
    }

    @Test
    fun `Test method creates correctly`() {
        val method = Method("test(Lay;JDF)V")
        check(method.name == "test") { "Methods name should have been 'test'" }
        check(
            checkType(
                method.argumentTypes[0],
                Type.getType("Lay;")
            )
        ) { "First method argument should have been 'Lay;'" }
        check(checkType(method.argumentTypes[3], Type.FLOAT_TYPE)) { "4th Method argument should have been 'float'" }

        check(checkType(method.returnType, Type.VOID_TYPE)) { "Return type should have been 'void'" }
    }

    @Test
    fun `Test method without return type throws`() {
        val r = runCatching {
            Method("test(Lay;JDF)")
        }
        check(r.isFailure) {"Invalid signature generated correctly? Should not have."}
    }
}