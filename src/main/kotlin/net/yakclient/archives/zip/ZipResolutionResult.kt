package net.yakclient.archives.zip

import net.yakclient.archives.ResolutionResult
import net.yakclient.archives.ArchiveHandle

public data class ZipResolutionResult(
    override val archive: ArchiveHandle
) : ResolutionResult