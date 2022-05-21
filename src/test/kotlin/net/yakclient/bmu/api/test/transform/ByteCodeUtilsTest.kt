package net.yakclient.bmu.api.test.transform

import net.yakclient.bmu.api.transform.MethodSignature
import org.junit.jupiter.api.Test

class ByteCodeUtilsTest {
    @Test
    fun `Test MethodSignature`() {
       println(MethodSignature.of("test(ZZZZ)"))
    }
}