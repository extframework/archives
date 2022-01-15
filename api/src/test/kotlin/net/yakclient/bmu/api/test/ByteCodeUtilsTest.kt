package net.yakclient.bmu.api.test

import net.yakclient.bmu.api.ByteCodeUtils
import org.junit.jupiter.api.Test

class ByteCodeUtilsTest {
    @Test
    fun `Test MethodSignature`() {
       println(ByteCodeUtils.MethodSignature.of("test(ZZZZ)"))
    }
}