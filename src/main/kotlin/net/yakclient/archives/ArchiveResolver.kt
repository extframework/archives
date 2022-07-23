package net.yakclient.archives

import kotlin.reflect.KClass

public fun interface ClassLoaderProvider<R: ArchiveReference> : (R) -> ClassLoader

public interface ArchiveResolver<T : ArchiveReference, R: ResolutionResult> {
    public val type: KClass<T>

    public fun resolve(
        archiveRefs: List<T>,
        clProvider: ClassLoaderProvider<T>,
        parents: Set<ArchiveHandle>,
    ): List<R>
}