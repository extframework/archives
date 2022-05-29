package net.yakclient.archives

import net.yakclient.archives.internal.jpm.JpmFinder
import net.yakclient.archives.internal.jpm.JpmResolver
import net.yakclient.archives.internal.zip.ZipFinder
import net.yakclient.archives.internal.zip.ZipResolver
import net.yakclient.archives.transform.ClassResolver
import net.yakclient.archives.transform.TransformerConfig
import net.yakclient.common.util.CAST
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter

import java.nio.file.Path
import kotlin.reflect.full.isSubclassOf

private typealias Finder = ArchiveFinder<ArchiveHandle>
private typealias Resolver = ArchiveResolver<ArchiveHandle>

public object Archives {
    public enum class Resolvers(
        delegate: ArchiveResolver<*>
    ) : ArchiveResolver<ArchiveHandle> by delegate as ArchiveResolver<ArchiveHandle> {
        JPM_RESOLVER(JpmResolver()),
        ZIP_RESOLVER(ZipResolver());

        private val resolver: ArchiveResolver<ArchiveHandle> = delegate as ArchiveResolver<ArchiveHandle>

        override fun resolve(
            archiveRefs: List<ArchiveHandle>,
            clProvider: ClassLoaderProvider<ArchiveHandle>,
            parents: Set<ResolvedArchive>
        ): List<ResolvedArchive> {
            check(archiveRefs.all { it::class.isSubclassOf(type) })

            return resolver.resolve(archiveRefs, clProvider, parents)
        }
    }


    public enum class Finders(
        public val delegate: ArchiveFinder<*>
    ) : ArchiveFinder<ArchiveHandle> by delegate as ArchiveFinder<ArchiveHandle> {
        JPM_FINDER(JpmFinder()),
        ZIP_FINDER(ZipFinder());
    }

    public fun <T : ArchiveHandle> find(path: Path, finder: ArchiveFinder<T>): T = finder.find(path)

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