package net.yakclient.archives

public interface ResolvedArchive {
    public val classloader: ClassLoader
    public val packages: Set<String>
    public val parents: Set<ResolvedArchive>
    public val name: String?
}