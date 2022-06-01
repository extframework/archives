package net.yakclient.archives.jpm

import net.yakclient.archives.JpmArchives
import net.yakclient.archives.JpmArchives.archives
import net.yakclient.archives.JpmArchives.nameToArchive
import net.yakclient.archives.ResolvedArchive
import java.lang.module.Configuration
import java.lang.module.ModuleDescriptor

internal class ResolvedJpm internal constructor(
    val module: Module,
) : ResolvedArchive {
    override val classloader: ClassLoader = module.classLoader ?: ClassLoader.getSystemClassLoader()
    override val packages: Set<String> = module.packages
    override val parents: Set<ResolvedArchive>
    override val name: String = module.name
    val configuration: Configuration = module.layer.configuration()
    val layer: ModuleLayer = module.layer

    init {
        archives[module.name] = this

        fun ModuleLayer.allModules(): Set<Module> = modules() + parents().flatMap { it.allModules() }

        fun loadArchive(name: String): ResolvedArchive? =
            nameToArchive[name] ?: module.layer.allModules().find { it.name == name }?.let(::ResolvedJpm)

        parents = if (module.descriptor.isAutomatic)
            module.layer.allModules().mapTo(HashSet(), JpmArchives::moduleToArchive)
        else module.descriptor.requires()
            .filterNot {
                it.modifiers().contains(ModuleDescriptor.Requires.Modifier.STATIC)
            }.mapTo(HashSet()) {
                loadArchive(it.name())!!
            }

    }
}