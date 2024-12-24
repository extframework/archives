package dev.extframework.archives.zip

import dev.extframework.archives.ArchiveReference
import java.net.URI
import java.util.jar.JarFile

public class ZipReference(
    public val zip: JarFile,
    override val location: URI,
) : ArchiveReference {
    private var closed = false
    private val overrides: MutableMap<String, ArchiveReference.Entry> = HashMap()
    private val removes: MutableSet<String> = HashSet()

    override val reader: ArchiveReference.Reader = ZipReader()
    override val writer: ArchiveReference.Writer = ZipWriter()
    override val name: String? = null
    override val modified: Boolean = overrides.isNotEmpty() || removes.isNotEmpty()
    override val isClosed: Boolean
        get() = closed

    override fun close() {
        closed = true
        zip.close()
    }

    private fun ensureOpen() {
        if (closed) {
            throw IllegalStateException("ZipReference is closed")
        }
    }

    private inner class ZipReader : ArchiveReference.Reader {
        override fun of(name: String): ArchiveReference.Entry? {
            ensureOpen()

            return (overrides[name] ?: zip.getJarEntry(name)?.let { entry ->
                ArchiveReference.Entry(
                    entry.name,
                    entry.isDirectory,
                    this@ZipReference
                ) {
                    zip.getInputStream(entry)
                }
            }).takeUnless { removes.contains(name) }
        }

        override fun entries(): Sequence<ArchiveReference.Entry> {
            val alreadyRead = HashSet<String>()

            return (overrides.keys.asSequence() + zip.entries().asSequence().map { it.name })
                .filter(alreadyRead::add).mapNotNull(::of).onEach { ensureOpen() }
        }
    }

    private inner class ZipWriter : ArchiveReference.Writer {
        override fun put(entry: ArchiveReference.Entry) {
            ensureOpen()
            overrides[entry.name] = entry
        }

        override fun remove(name: String) {
            ensureOpen()
            removes.add(name)
        }
    }
}