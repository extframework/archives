package net.yakclient.archives.internal.zip

import net.yakclient.archives.ResolvedArchive


internal class ResolvedZip(
    override val classloader: ClassLoader,
    override val packages: Set<String>,
    override val parents: Set<ResolvedArchive>
) : ResolvedArchive {
//    override fun loadService(name: String): Nothing =
//        throw UnsupportedOperationException("Loading services from ResolvedArchiveReference is not supported")
}