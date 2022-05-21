package net.yakclient.archives

import kotlin.reflect.KClass

public fun interface ClassLoaderProvider<R: ArchiveHandle> : (R) -> ClassLoader

public interface ArchiveResolver<T : ArchiveHandle> {
    public val type: KClass<T>

    public fun resolve(archiveRefs: List<T>, clProvider: ClassLoaderProvider<T>, parents: List<ResolvedArchive>): List<ResolvedArchive>
}