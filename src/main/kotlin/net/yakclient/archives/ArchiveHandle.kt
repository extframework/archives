package net.yakclient.archives

import java.io.InputStream

public interface ArchiveHandle : ArchiveTree {
    public val classloader: ClassLoader
    public val packages: Set<String>
    public val parents: Set<ArchiveHandle>
    public val name: String?

    override fun getResource(name: String): InputStream? = classloader.getResourceAsStream(name)
}