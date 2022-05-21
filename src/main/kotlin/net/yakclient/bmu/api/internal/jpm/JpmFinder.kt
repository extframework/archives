package net.yakclient.bmu.api.internal.jpm

import net.yakclient.bmu.api.ArchiveFinder
import java.lang.module.ModuleFinder
import java.nio.file.Files
import java.nio.file.Path
import kotlin.reflect.KClass

internal class JpmFinder : ArchiveFinder<JpmHandle> {
    override val type: KClass<JpmHandle> = JpmHandle::class

    override fun find(path: Path): JpmHandle {
        assert(!Files.isDirectory(path)) { "Cannot load directory of archives from path: $path" }
        return JpmHandle(
            ModuleFinder.of(path).findAll()?.firstOrNull()
                ?: throw IllegalArgumentException("Failed to find archives in path: $path")
        )
    }

}