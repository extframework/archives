package dev.extframework.archives.test.zip

import com.durganmcbroom.resources.Resource
import com.durganmcbroom.resources.ResourceStream
import com.durganmcbroom.resources.asResourceStream
import dev.extframework.archives.ArchiveReference
import dev.extframework.archives.zip.ZipReference
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.FileOutputStream
import java.net.URL
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
                object : Resource {
                    override val location: String = setupEmptyJar.toString()

                    override fun open(): ResourceStream {
                        return ByteArrayInputStream(byteArrayOf()).asResourceStream()
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
                object : Resource {
                    override val location: String = setupEmptyJar.toString()
                    override fun open(): ResourceStream {
                        return ByteArrayInputStream(byteArrayOf()).asResourceStream()
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

        val nanResource = object: Resource {
            override val location: String
                get() = throw UnsupportedOperationException("")

            override fun open(): ResourceStream {
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

    @Test
    fun `Entry returns correct location`() {
        val setupEmptyJar = setupJar(
            "c" to "Go watch",
            "a" to "A jazz musician",
            "b" to "Named: ",
            "e" to "Patrick Bartley"
        )
        val archive = ZipReference(JarFile(setupEmptyJar.toFile()), setupEmptyJar.toUri())

        check(String(URL(archive.reader["c"]!!.resource.location).openStream().readAllBytes()) == "Go watch")
        check(String(URL(archive.reader["e"]!!.resource.location).openStream().readAllBytes()) == "Patrick Bartley")

    }
}