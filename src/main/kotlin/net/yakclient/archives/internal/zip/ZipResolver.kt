package net.yakclient.archives.internal.zip

import net.yakclient.archives.ArchiveResolver
import net.yakclient.archives.ClassLoaderProvider
import net.yakclient.archives.ResolvedArchive
import kotlin.reflect.KClass

internal class ZipResolver : ArchiveResolver<ZipHandle> {
    override val type: KClass<ZipHandle> = ZipHandle::class

    override fun resolve(
        archiveRefs: List<ZipHandle>,
        clProvider: ClassLoaderProvider<ZipHandle>,
        parents: Set<ResolvedArchive>
    ): List<ResolvedArchive> = archiveRefs.map { it to clProvider(it) }.map { entry ->
        val packages = entry.first.reader.entries()
            .map { it.name } // Mapping to the name of the entry
            .filter { it.endsWith(".class") } // Ensuring only java classes
            .filterNot { it == "module-info.class" } // Don't want the module-info
            .map { it.replace('/', '.').removeSuffix(".class") } // Normalizing names to appropriate class names
            .mapTo(HashSet()) { c -> c.substring(0, c.lastIndexOf('.').let { if (it == -1) 0 else it }) } // Mapping to a package names within a HashSet

        ResolvedZip(entry.second, packages, parents)
    }
}