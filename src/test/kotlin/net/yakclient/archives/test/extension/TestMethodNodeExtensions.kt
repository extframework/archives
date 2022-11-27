package net.yakclient.archives.test.extension

import net.yakclient.archives.extension.parameterClasses
import org.junit.jupiter.api.Test

class TestMethodNodeExtensions {
    @Test
    fun `test description to parameters`() {
        println(parameterClasses("(BZCSBIFJLnet/yakclient/archives/test/extension/TestMethodNodeExtensions;[B[[Z[Lnet/yakclient/archives/test/extension/TestMethodNodeExtensions;BB)") {
            kotlin.runCatching { this::class.java.classLoader.loadClass(it) }.getOrNull() ?: Class.forName(it)
        })
    }

}