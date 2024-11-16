package dev.extframework.archives.zip

import dev.extframework.archives.ArchiveResolver
import dev.extframework.archives.ClassLoaderProvider
import dev.extframework.archives.ArchiveHandle
import kotlin.reflect.KClass

public object ZipResolver : ArchiveResolver<ZipReference, ZipResolutionResult> {
    override val type: KClass<ZipReference> = ZipReference::class

    override fun resolve(
        archiveRefs: List<ZipReference>,
        clProvider: ClassLoaderProvider<ZipReference>,
        parents: Set<ArchiveHandle>,
    ): List<ZipResolutionResult> = archiveRefs.map { it to clProvider(it) }.map { entry ->
        val packages = entry.first.reader.entries()
            .map { it.name } // Mapping to the name of the entry
            .filter { it.endsWith(".class") } // Ensuring only java classes
            .filterNot { it.endsWith("module-info.class") } // Don't want the module-info
            .map { it.replace('/', '.').removeSuffix(".class") } // Normalizing names to appropriate class names
            .mapTo(HashSet()) { c -> c.substring(0, c.lastIndexOf('.').let { if (it == -1) 0 else it }) } // Mapping to a package names within a HashSet

        val archive = ZipHandle(entry.second, packages, parents)

        ZipResolutionResult(archive)
    }
}