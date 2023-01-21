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
    private fun setupEmptyJar() : Path {
        val createTempFile = Files.createTempFile(UUID.randomUUID().toString(), ".jar")
        JarOutputStream(FileOutputStream(createTempFile.toFile())).use { target ->
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
        val setupEmptyJar = setupEmptyJar()
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
}