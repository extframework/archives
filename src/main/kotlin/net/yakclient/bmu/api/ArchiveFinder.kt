package net.yakclient.bmu.api

import java.nio.file.Path
import kotlin.reflect.KClass

public interface ArchiveFinder<T : ArchiveHandle> {
    public val type: KClass<T>

    public fun find(path: Path): T
}