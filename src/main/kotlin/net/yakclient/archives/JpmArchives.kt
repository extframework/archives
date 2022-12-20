package net.yakclient.archives

import net.yakclient.archives.jpm.JpmHandle

public object JpmArchives {
    internal val archives: MutableMap<String, JpmHandle> = HashMap()
    public val nameToArchive: Map<String, ArchiveHandle>
        get() = archives.toMap()

    public fun moduleToArchive(module: Module): ArchiveHandle {
        return nameToArchive[module.name] ?: JpmHandle(module)
    }

    public fun archiveToModule(handle: ArchiveHandle): Module? =
        if (handle is JpmHandle) handle.module else null
}