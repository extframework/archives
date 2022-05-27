package net.yakclient.archives

import net.yakclient.archives.internal.jpm.JpmFinder
import net.yakclient.archives.internal.jpm.JpmResolver
import net.yakclient.archives.internal.zip.ZipFinder
import net.yakclient.archives.internal.zip.ZipResolver
import net.yakclient.archives.transform.ClassResolver
import net.yakclient.archives.transform.TransformerConfig
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter

import java.nio.file.Path

private typealias Finder = ArchiveFinder<ArchiveHandle>
private typealias Resolver = ArchiveResolver<ArchiveHandle>

public object Archives {
    public enum class Resolvers(
        public val resolver: Resolver
    ) {
        JPM_RESOLVER(JpmResolver() as Resolver),
        ZIP_RESOLVER(ZipResolver() as Resolver)
    }


    public enum class Finders(
        public val finder: Finder
    ) {
        JPM_FINDER(JpmFinder() as Finder),
        ZIP_FINDER(ZipFinder() as Finder)
    }

    public fun <T: ArchiveHandle> find(path: Path, finder: ArchiveFinder<T>): T = finder.find(path)

    @JvmOverloads
    public fun <T : ArchiveHandle> resolve(
        refs: List<T>,
        resolver: ArchiveResolver<T>,
        parents: Set<ResolvedArchive> = hashSetOf(),
        clProvider: ClassLoaderProvider<T>,
    ): List<ResolvedArchive> = resolver.resolve(refs, clProvider, parents)

    @JvmOverloads
    public fun <T : ArchiveHandle> resolve(
        ref: T,
        classloader: ClassLoader,
        resolver: ArchiveResolver<T>,
        parents: Set<ResolvedArchive> = hashSetOf(),
    ): ResolvedArchive = resolve(listOf(ref), resolver, parents) { classloader }.first()

    public const val WRITER_FLAGS: Int = ClassWriter.COMPUTE_FRAMES

    @JvmOverloads
    public fun resolve(reader: ClassReader, config: TransformerConfig, writer : ClassWriter = ClassWriter(WRITER_FLAGS)): ByteArray {
        val resolver = ClassResolver(writer, config)
        reader.accept(resolver, 0)

        return writer.toByteArray()
    }
}