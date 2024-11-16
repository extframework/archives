package dev.extframework.archives

import dev.extframework.archives.transform.TransformerConfig
import dev.extframework.archives.zip.ZipFinder
import dev.extframework.archives.zip.ZipResolutionResult
import dev.extframework.archives.zip.ZipResolver
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.tree.ClassNode
import java.nio.file.Path

public object Archives {
    public object Resolvers {
        @Deprecated(message = "Replace with ZipResolver call directly")
        public val ZIP_RESOLVER: ArchiveResolver<ArchiveReference, ZipResolutionResult> =
            ZipResolver as ArchiveResolver<ArchiveReference, ZipResolutionResult>
    }

    public object Finders {
        @Deprecated(message = "Replace with ZipFinder call directly")
        public val ZIP_FINDER: ArchiveFinder<*> = ZipFinder
    }

    public fun <T : ArchiveReference> find(path: Path, finder: ArchiveFinder<T>): T = finder.find(path)

    @JvmOverloads
    public fun <T : ArchiveReference, R : ResolutionResult> resolve(
        refs: List<T>,
        resolver: ArchiveResolver<T, R>,
        parents: Set<ArchiveHandle> = hashSetOf(),
        clProvider: ClassLoaderProvider<T>,
    ): List<R> = resolver.resolve(refs, clProvider, parents)

    @JvmOverloads
    public fun <T : ArchiveReference, R : ResolutionResult> resolve(
        ref: T,
        classloader: ClassLoader,
        resolver: ArchiveResolver<T, R>,
        parents: Set<ArchiveHandle> = hashSetOf(),
    ): R = resolve(listOf(ref), resolver, parents) { classloader }.first()

    public const val WRITER_FLAGS: Int = ClassWriter.COMPUTE_FRAMES

    @JvmOverloads
    public fun applyConfig(
        reader: ClassReader,
        config: TransformerConfig,
        writer: ClassWriter = ClassWriter(WRITER_FLAGS),
        readerFlags: Int = 0
    ): ByteArray {
        var node = ClassNode()
        reader.accept(node, readerFlags)

        node = config.ct(node)
        node.methods = node.methods.map {
            config.mt.invoke(it)
        }
        node.fields = node.fields.map {
            config.ft.invoke(it)
        }

        node.accept(writer)
        return writer.toByteArray()
    }
}