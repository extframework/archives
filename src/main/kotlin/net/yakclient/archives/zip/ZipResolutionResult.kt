package net.yakclient.archives.zip

import net.yakclient.archives.ResolutionResult
import net.yakclient.archives.ResolvedArchive

public data class ZipResolutionResult(
    override val archive: ResolvedArchive
) : ResolutionResult