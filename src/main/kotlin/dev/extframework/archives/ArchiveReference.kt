package dev.extframework.archives

import dev.extframework.archives.Archives.WRITER_FLAGS
import dev.extframework.archives.transform.AwareClassWriter
import dev.extframework.archives.transform.TransformerConfig
import org.objectweb.asm.ClassReader
import java.io.ByteArrayInputStream
import java.io.Closeable
import java.io.InputStream
import java.net.URI

public interface ArchiveReference : Closeable, ArchiveTree {
    public val location: URI
    public val reader: Reader
    public val writer: Writer
    public val name: String?

    public val modified: Boolean
    public val isClosed: Boolean
    public val isOpen: Boolean
        get() = !isClosed

    override fun getResource(name: String): InputStream? {
        return reader[name]?.open()
    }

    public interface Reader {
        public fun of(name: String): Entry?

        public fun contains(name: String): Boolean = get(name) != null

        public fun entries(): Sequence<Entry>

        public operator fun get(name: String): Entry? = of(name)
    }

    public interface Writer {
        public fun put(
            name: String,
            handle: ArchiveReference,
            isDirectory: Boolean = false,
            stream: () -> InputStream
        ): Unit =
            put(Entry(name, isDirectory, handle, stream))

        public fun put(entry: Entry)

        public fun remove(name: String)
    }

    public data class Entry public constructor(
        public val name: String,
        public val isDirectory: Boolean,
        public val handle: ArchiveReference,
        private val resource: () -> InputStream,
    ) {
        @Deprecated("Dont use this, do it manually.")
        public fun transform(config: TransformerConfig, handles: List<ArchiveTree> = listOf()): Entry {
            return Entry(
                name,
                isDirectory,
                handle
            ) {
                ByteArrayInputStream(
                    Archives.applyConfig(
                        ClassReader(open()),
                        config,
                        AwareClassWriter(handles + handle, WRITER_FLAGS)
                    )
                )
            }
        }

        public fun open(): InputStream {
            return resource()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Entry) return false

            if (name != other.name) return false

            return true
        }

        override fun hashCode(): Int {
            return name.hashCode()
        }
    }
}