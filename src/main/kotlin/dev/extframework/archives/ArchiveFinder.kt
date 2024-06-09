package dev.extframework.archives

import java.nio.file.Path
import kotlin.reflect.KClass

public interface ArchiveFinder<T : ArchiveReference> {
    public val type: KClass<T>

    public fun find(path: Path): T
}