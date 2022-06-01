package net.yakclient.archives.jpm

import net.yakclient.archives.ResolutionResult
import net.yakclient.archives.ResolvedArchive
import java.lang.ModuleLayer.Controller

public data class JpmResolutionResult(
    override val archive: ResolvedArchive,
    public val controller: Controller,
    public val module: Module
) : ResolutionResult
