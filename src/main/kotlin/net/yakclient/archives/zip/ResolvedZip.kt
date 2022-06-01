package net.yakclient.archives.zip

import net.yakclient.archives.ResolvedArchive

internal class ResolvedZip(
    override val classloader: ClassLoader,
    override val packages: Set<String>,
    override val parents: Set<ResolvedArchive>
) : ResolvedArchive {
    override val name: String? = null
}