package net.yakclient.archives.test.zip

import net.yakclient.archives.ArchiveReference
import net.yakclient.archives.zip.ZipReference
import net.yakclient.common.util.resource.SafeResource
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream

class TestZipArchives {
    private fun setupJar(
        vararg entries: Pair<String, String>
    ) : Path {
        val createTempFile = Files.createTempFile(UUID.randomUUID().toString(), ".jar")
        JarOutputStream(FileOutputStream(createTempFile.toFile())).use { target ->
            for (entry in entries) {
                target.putNextEntry(JarEntry(entry.first))

                target.write(entry.second.toByteArray())

                target.closeEntry()
            }
//            val entry = JarEntry()
//
//            target.putNextEntry(entry)
//
//            archive.reader.entries().forEach { e ->
//                val entry = JarEntry(e.name)
//
//                target.putNextEntry(entry)
//
//                val eIn = e.resource.open()
//
//                //Stolen from https://stackoverflow.com/questions/1281229/how-to-use-jaroutputstream-to-create-a-jar-file
//                val buffer = ByteArray(1024)
//
//                while (true) {
//                    val count: Int = eIn.read(buffer)
//                    if (count == -1) break
//
//                    target.write(buffer, 0, count)
//                }
//
//                target.closeEntry()
//            }
        }

        return createTempFile
    }

    @Test
    fun `Create zip archive`() {
        val setupEmptyJar = setupJar()
        val zip = ZipReference(JarFile(setupEmptyJar.toFile()), setupEmptyJar.toUri())

        check(zip.reader.entries().sumOf { 1L } == 0L)

        zip.writer.put(
            ArchiveReference.Entry(
                "test",
                object : SafeResource {
                    override val uri: URI = setupEmptyJar.toUri()

                    override fun open(): InputStream {
                        return ByteArrayInputStream(byteArrayOf())
                    }
                },
                false,
                zip
            )
        )

        check(zip.reader.entries().sumOf { 1L } == 1L)
    }

    @Test
    fun `Remove from zip archive`() {
        val setupEmptyJar = setupJar()
        val zip = ZipReference(JarFile(setupEmptyJar.toFile()), setupEmptyJar.toUri())

        check(zip.reader.entries().sumOf { 1L } == 0L)

        zip.writer.put(
            ArchiveReference.Entry(
                "test",
                object : SafeResource {
                    override val uri: URI = setupEmptyJar.toUri()

                    override fun open(): InputStream {
                        return ByteArrayInputStream(byteArrayOf())
                    }
                },
                false,
                zip
            )
        )

        zip.writer.remove("test")

        check(zip.reader.entries().sumOf { 1L } == 0L)
    }

    @Test
    fun `Test no duplicates`() {
        val setupEmptyJar = setupJar(
            "c" to "Go watch",
            "a" to "A jazz musician",
            "b" to "Named: ",
            "e" to "Patrick Bartley"
        )
        val archive = ZipReference(JarFile(setupEmptyJar.toFile()), setupEmptyJar.toUri())

        val nanResource = object: SafeResource {
            override val uri: URI
                get() = throw UnsupportedOperationException("")

            override fun open(): InputStream {
                throw UnsupportedOperationException("")
            }
        }

        fun ZipReference.put(name: String) {
            writer.put(ArchiveReference.Entry(
                name, nanResource, false, archive
            ))
        }

        archive.put("c")
        archive.put("a")
        archive.put("d")

        val read = HashSet<String>()

        archive.reader.entries().forEach {
            assert(read.add(it.name)) {"Duplicates found!"}
        }
    }
}