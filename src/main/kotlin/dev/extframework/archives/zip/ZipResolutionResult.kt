package dev.extframework.archives.zip

import dev.extframework.archives.ResolutionResult
import dev.extframework.archives.ArchiveHandle

public data class ZipResolutionResult(
    override val archive: ArchiveHandle
) : ResolutionResult