package net.yakclient.bmu.api

public interface ResolvedArchive {
    public val classloader: ClassLoader
    public val packages: Set<String>
    public val parents: List<ResolvedArchive>

    public fun loadService(name: String) : List<Class<*>>
}