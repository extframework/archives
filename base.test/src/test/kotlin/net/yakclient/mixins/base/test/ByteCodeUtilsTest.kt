package net.yakclient.mixins.base.test

import net.yakclient.mixins.base.internal.bytecode.ByteCodeUtils
import org.junit.jupiter.api.Test

class ByteCodeUtilsTest {
    @Test
    fun `Test MethodSignature`() {
       println(ByteCodeUtils.MethodSignature.of("test(ZZZZ)"))
    }
}