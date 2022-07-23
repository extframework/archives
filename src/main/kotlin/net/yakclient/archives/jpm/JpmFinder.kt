package net.yakclient.archives.jpm

import net.yakclient.archives.ArchiveFinder
import java.lang.module.ModuleFinder
import java.nio.file.Files
import java.nio.file.Path
import kotlin.reflect.KClass

internal class JpmFinder : ArchiveFinder<JpmReference> {
    override val type: KClass<JpmReference> = JpmReference::class

    override fun find(path: Path): JpmReference {
        assert(!Files.isDirectory(path)) { "Cannot load directory of archives from path: $path" }
        return JpmReference(
            ModuleFinder.of(path).findAll()?.firstOrNull()
                ?: throw IllegalArgumentException("Failed to find archives in path: $path")
        )
    }

}