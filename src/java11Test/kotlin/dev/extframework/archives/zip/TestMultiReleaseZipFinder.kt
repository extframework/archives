package dev.extframework.archives.zip

import dev.extframework.archives.module.JpmFinder
import dev.extframework.archives.module.JpmReference
import org.junit.jupiter.api.Test
import kotlin.io.path.Path

class TestMultiReleaseZipFinder {
    @Test
    fun `Test reads correctly`() {
        val path = Path(this::class.java.classLoader.getResource("log4j-api.jar")!!.path)

        val ref: JpmReference = JpmFinder.find(path)

        val res = ref.reader["org/apache/logging/log4j/util/internal/DefaultObjectInputFilter.class"]

        check(res != null)
    }
}