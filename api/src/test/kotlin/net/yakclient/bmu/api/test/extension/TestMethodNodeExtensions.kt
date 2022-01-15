package net.yakclient.bmu.api.test.extension

import net.yakclient.bmu.api.extension.compiledDescriptionOf
import org.junit.jupiter.api.Test

class TestMethodNodeExtensions {
    @Test
    fun `test description to parameters`() {
        println(compiledDescriptionOf("(BZCSBIFJLnet/yakclient/mixins/base/test/extension/TestMethodNodeExtensions;[B[[Z[Lnet/yakclient/mixins/base/test/extension/TestMethodNodeExtensions;BB)"))
    }

}