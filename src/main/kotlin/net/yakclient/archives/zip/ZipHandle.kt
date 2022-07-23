package net.yakclient.archives.zip

import net.yakclient.archives.ArchiveHandle

internal class ZipHandle(
    override val classloader: ClassLoader,
    override val packages: Set<String>,
    override val parents: Set<ArchiveHandle>
) : ArchiveHandle {
    override val name: String? = null
}