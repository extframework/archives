package net.yakclient.archives

import net.yakclient.archives.jpm.JpmFinder
import net.yakclient.archives.jpm.JpmResolutionResult
import net.yakclient.archives.jpm.JpmResolver
import net.yakclient.archives.transform.TransformerConfig
import net.yakclient.archives.zip.ZipFinder
import net.yakclient.archives.zip.ZipResolutionResult
import net.yakclient.archives.zip.ZipResolver
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.tree.ClassNode
import java.nio.file.Path

public object Archives {
    public object Resolvers {
        public val JPM_RESOLVER: ArchiveResolver<ArchiveReference, JpmResolutionResult> =
            JpmResolver() as ArchiveResolver<ArchiveReference, JpmResolutionResult>
        public val ZIP_RESOLVER: ArchiveResolver<ArchiveReference, ZipResolutionResult> =
            ZipResolver() as ArchiveResolver<ArchiveReference, ZipResolutionResult>
    }

    public object Finders {
        public val JPM_FINDER: ArchiveFinder<*> = JpmFinder()
        public val ZIP_FINDER: ArchiveFinder<*> = ZipFinder()
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
    public fun resolve(
        reader: ClassReader,
        config: TransformerConfig,
        writer: ClassWriter = ClassWriter(WRITER_FLAGS)
    ): ByteArray {
        val node = ClassNode()
        reader.accept(node, 0)

        config.ct(node)
        node.methods.forEach(config.mt)
        node.fields.forEach(config.ft)

        node.accept(writer)
        return writer.toByteArray()
    }
}