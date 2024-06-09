package dev.extframework.archives.zip

import dev.extframework.archives.ArchiveHandle

internal class ZipHandle(
    override val classloader: ClassLoader,
    override val packages: Set<String>,
    override val parents: Set<ArchiveHandle>
) : ArchiveHandle {
    override val name: String? = null
}