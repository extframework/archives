package dev.extframework.archives

import com.durganmcbroom.resources.Resource
import com.durganmcbroom.resources.openStream
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
        return  reader[name]?.resource?.openStream()
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
            resource: Resource,
            handle: ArchiveReference,
            isDirectory: Boolean = false
        ): Unit =
            put(Entry(name, resource, isDirectory, handle))

        public fun put(entry: Entry)

        public fun remove(name: String)
    }

    public data class Entry public constructor(
        public val name: String,
        public val resource: Resource,
        public val isDirectory: Boolean,
        public val handle: ArchiveReference,
    ) {
        public fun transform(config: TransformerConfig, handles: List<ArchiveTree> = listOf()): Entry {
            return Entry(
                name,
                Resource(resource.location) {
                    ByteArrayInputStream(
                        Archives.applyConfig(
                            ClassReader(resource.openStream()),
                            config,
                            AwareClassWriter(handles + handle, WRITER_FLAGS)
                        )
                    )
                },
                isDirectory,
                handle
            )
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