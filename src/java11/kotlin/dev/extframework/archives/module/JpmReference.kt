package dev.extframework.archives.module

import com.durganmcbroom.resources.openStream
import com.durganmcbroom.resources.streamToResource
import dev.extframework.archives.ArchiveReference
import dev.extframework.common.util.readInputStream
import java.io.File
import java.io.InputStream
import java.lang.module.ModuleReader
import java.lang.module.ModuleReference
import java.net.URI
import java.nio.ByteBuffer
import java.nio.file.Paths
import java.util.*
import java.util.function.Predicate
import java.util.stream.Stream
import kotlin.io.path.Path

public class JpmReference(
    delegate: ModuleReference,
) : ArchiveReference, ModuleReference(
    delegate.descriptor(),
    delegate.location().orElseGet { null }
) {
    private var closed: Boolean = false
    private val overrides: MutableMap<String, ArchiveReference.Entry> = HashMap()
    private val removes: MutableSet<String> = HashSet()
    override val location: URI = delegate.location().get()
    override val reader: ArchiveReference.Reader = JpmReader(delegate.open())
    override val writer: ArchiveReference.Writer = JpmWriter()
    override val name: String = descriptor().name()
    override val modified: Boolean get() = overrides.isNotEmpty() || removes.isNotEmpty()
    override val isClosed: Boolean
        get() = closed

    override fun close() {
        closed = true
        (reader as JpmReader).close()
    }

    private fun ensureOpen() {
        if (closed) {
            throw IllegalStateException("Module is closed")
        }
    }

    override fun open(): ModuleReader = reader as ModuleReader
    private inner class JpmReader(
        private val reader: ModuleReader
    ) : ArchiveReference.Reader, ModuleReader by reader {
        private val cache: MutableMap<String, ArchiveReference.Entry> = HashMap()
        override fun of(name: String): ArchiveReference.Entry? {
            ensureOpen()
            return (overrides[name]
                ?: cache[name]
                ?: reader.find(name).orElse(null)?.let {
                    ArchiveReference.Entry(
                        name,
                        name.endsWith("/"), // This is how they do it in the java source code, wish there could be a better solution :(
                        this@JpmReference
                    ) {
                        it.toURL().openStream()
                    }
                }?.also { cache[name] = it })
                ?.takeUnless { removes.contains(it.name) }
        }

        override fun entries(): Sequence<ArchiveReference.Entry> =
            Sequence(list()::iterator).mapNotNull(::of)

        override fun find(name: String): Optional<URI> =
            Optional.ofNullable(of(name)?.name?.let { URI.create("jar:${Paths.get(location)}!${File.separatorChar}$it") })

        override fun open(name: String): Optional<InputStream> =
            Optional.ofNullable(of(name)?.open())

        override fun read(name: String): Optional<ByteBuffer> =
            Optional.ofNullable(
                of(name)?.open()?.readInputStream()?.let { ByteBuffer.wrap(it) })

        override fun list(): Stream<String> {
            ensureOpen()
            return Stream.concat(overrides.keys.stream(), reader.list()).filter(
                object : Predicate<String> {
                    private val processed = mutableSetOf<String>()
                    override fun test(t: String): Boolean = processed.add(t)
                }
            )
        }
    }

    private inner class JpmWriter : ArchiveReference.Writer {
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