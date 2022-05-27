package net.yakclient.archives

import net.yakclient.archives.internal.jpm.ResolvedJpm

public object JpmArchives {
    internal val archives: MutableMap<String, ResolvedJpm> = HashMap()
    public val nameToArchive: Map<String, ResolvedArchive>
        get() = archives.toMap()

    public fun moduleToArchive(module: Module) : ResolvedArchive {
        return nameToArchive[module.name] ?: ResolvedJpm(module)
    }
}