package net.yakclient.archives

public interface ArchiveHandle {
    public val classloader: ClassLoader
    public val packages: Set<String>
    public val parents: Set<ArchiveHandle>
    public val name: String?
}