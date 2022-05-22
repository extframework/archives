package net.yakclient.archives.test.transform

import net.yakclient.archives.transform.MethodSignature
import org.junit.jupiter.api.Test

class ByteCodeUtilsTest {
    @Test
    fun `Test MethodSignature`() {
       println(MethodSignature.of("test(ZZZZ)"))
        println(MethodSignature.of("()V"))
    }
}