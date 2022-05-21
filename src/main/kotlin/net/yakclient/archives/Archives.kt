package net.yakclient.archives

import net.yakclient.archives.internal.jpm.JpmFinder
import net.yakclient.archives.internal.jpm.JpmHandle
import net.yakclient.archives.internal.jpm.JpmResolver
import net.yakclient.archives.internal.zip.ZipFinder
import net.yakclient.archives.internal.zip.ZipHandle
import net.yakclient.archives.internal.zip.ZipResolver
import net.yakclient.archives.transform.ClassResolver
import net.yakclient.archives.transform.TransformerConfig
import net.yakclient.common.util.CAST
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter

import java.nio.file.Path
import java.util.*
import kotlin.reflect.KClass


public object Archives {
    public val jpmResolver: ArchiveResolver<JpmHandle> = JpmResolver()
    public val jpmFinder : ArchiveFinder<JpmHandle> = JpmFinder()

    public val zipResolver : ArchiveResolver<ZipHandle> = ZipResolver()
    public val zipFinder : ArchiveFinder<ZipHandle> = ZipFinder()

    @Suppress(CAST)
    private fun <T : ArchiveHandle> resolver(clazz: KClass<T>): ArchiveResolver<T> {
        return (ArchiveCatalog.loadService(ArchiveResolver::class)
            .firstOrNull { clazz == it.type } as? ArchiveResolver<T>)
            ?: throw IllegalStateException("Not able to load the ArchiveResolver, make sure all services are declared!")
    }

    public fun find(path: Path, finder: ArchiveFinder<*>): ArchiveHandle = finder.find(path)

    @JvmOverloads
    public fun <T : ArchiveHandle> resolve(
        refs: List<T>,
        resolver: ArchiveResolver<T> = run {
            val type = refs.first()::class
            check(refs.all { it::class == type }) { "All references must be of the same type!" }

            @Suppress(CAST)
            resolver(type) as ArchiveResolver<T>
        },
        parents: List<ResolvedArchive> = ArrayList(),
        clProvider: ClassLoaderProvider<T>,
    ): List<ResolvedArchive> = resolver.resolve(refs, clProvider, parents).onEach(ArchiveCatalog::catalog)

    @JvmOverloads
    public fun <T : ArchiveHandle> resolve(
        ref: T,
        classloader: ClassLoader,
        resolver: ArchiveResolver<T> = @Suppress(CAST) (resolver(ref::class) as ArchiveResolver<T>),
        parents: List<ResolvedArchive> = ArrayList(),
    ): ResolvedArchive = resolve(listOf(ref), resolver, parents) { classloader }.first()

    public const val WRITER_FLAGS: Int = ClassWriter.COMPUTE_FRAMES

    @JvmOverloads
    public fun resolve(reader: ClassReader, config: TransformerConfig, writer : ClassWriter = ClassWriter(WRITER_FLAGS)): ByteArray {
        val resolver = ClassResolver(writer, config)
        reader.accept(resolver, 0)

        return writer.toByteArray()
    }
}