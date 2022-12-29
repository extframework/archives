package net.yakclient.archives.test.extension

import net.yakclient.archives.extension.parameterClasses
import net.yakclient.archives.extension.parameters
import org.junit.jupiter.api.Test

class TestMethodNodeExtensions {
    @Test
    fun `test description to parameter classes`() {
        println(parameterClasses("(BZCSBIFJLnet/yakclient/archives/test/extension/TestMethodNodeExtensions;[B[[Z[Lnet/yakclient/archives/test/extension/TestMethodNodeExtensions;BB)") )
    }

    @Test
    fun `test description to parameters`() {
        println(parameters("(Lnet/yakclient/components/yak/test/extension/RealClass;ILkotlin/jvm/internal/DefaultConstructorMarker;)"))
    }

}