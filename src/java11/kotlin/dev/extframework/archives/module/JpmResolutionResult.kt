package dev.extframework.archives.module

import dev.extframework.archives.ResolutionResult
import dev.extframework.archives.ArchiveHandle
import java.lang.ModuleLayer.Controller

public data class JpmResolutionResult(
    override val archive: ArchiveHandle,
    public val controller: Controller,
    public val module: Module
) : ResolutionResult