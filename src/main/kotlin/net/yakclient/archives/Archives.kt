package net.yakclient.archives

import net.yakclient.archives.jpm.JpmFinder
import net.yakclient.archives.jpm.JpmResolutionResult
import net.yakclient.archives.jpm.JpmResolver
import net.yakclient.archives.transform.ClassResolver
import net.yakclient.archives.transform.TransformerConfig
import net.yakclient.archives.zip.ZipFinder
import net.yakclient.archives.zip.ZipResolutionResult
import net.yakclient.archives.zip.ZipResolver
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.nio.file.Path

public object Archives {
    public object Resolvers {
        public val JPM_RESOLVER: ArchiveResolver<ArchiveHandle, JpmResolutionResult> =
            JpmResolver() as ArchiveResolver<ArchiveHandle, JpmResolutionResult>
        public val ZIP_RESOLVER: ArchiveResolver<ArchiveHandle, ZipResolutionResult> =
            ZipResolver() as ArchiveResolver<ArchiveHandle, ZipResolutionResult>
    }

    public object Finders {
        public val JPM_FINDER: ArchiveFinder<*> = JpmFinder()
        public val ZIP_FINDER: ArchiveFinder<*> = ZipFinder()
    }

    public fun <T : ArchiveHandle> find(path: Path, finder: ArchiveFinder<T>): T = finder.find(path)

    @JvmOverloads
    public fun <T : ArchiveHandle, R : ResolutionResult> resolve(
        refs: List<T>,
        resolver: ArchiveResolver<T, R>,
        parents: Set<ResolvedArchive> = hashSetOf(),
        clProvider: ClassLoaderProvider<T>,
    ): List<R> = resolver.resolve(refs, clProvider, parents)

    @JvmOverloads
    public fun <T : ArchiveHandle, R : ResolutionResult> resolve(
        ref: T,
        classloader: ClassLoader,
        resolver: ArchiveResolver<T, R>,
        parents: Set<ResolvedArchive> = hashSetOf(),
    ): R = resolve(listOf(ref), resolver, parents) { classloader }.first()

    public const val WRITER_FLAGS: Int = ClassWriter.COMPUTE_FRAMES

    @JvmOverloads
    public fun resolve(
        reader: ClassReader,
        config: TransformerConfig,
        writer: ClassWriter = ClassWriter(WRITER_FLAGS)
    ): ByteArray {
        val resolver = ClassResolver(writer, config)
        reader.accept(resolver, 0)

        return writer.toByteArray()
    }
}