package net.yakclient.archives.impl.zip

import net.yakclient.archives.ResolvedArchive


public class ResolvedZip(
    override val classloader: ClassLoader,
    override val packages: Set<String>,
    override val parents: List<ResolvedArchive>
) : ResolvedArchive {
    override fun loadService(name: String): Nothing =
        throw UnsupportedOperationException("Loading services from ResolvedArchiveReference is not supported")
}