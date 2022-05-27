package net.yakclient.archives.internal.jpm

import net.yakclient.archives.JpmArchives.archives
import net.yakclient.archives.JpmArchives.nameToArchive
import net.yakclient.archives.ResolvedArchive
import java.lang.module.Configuration
import java.lang.module.ModuleDescriptor

internal class ResolvedJpm(
    val module: Module,
) : ResolvedArchive {
    override val classloader: ClassLoader = module.classLoader ?: ClassLoader.getSystemClassLoader()
    override val packages: Set<String> = module.packages
    override val parents: Set<ResolvedArchive>
    val configuration: Configuration = module.layer.configuration()
    val layer: ModuleLayer = module.layer
//    private val services: Map<String, List<Class<*>>> by lazy {
//        module.descriptor.provides().associate { it.service() to it.providers().map(classloader::loadClass) }
//    }

    init {
        archives[module.name] = this

        fun ModuleLayer.allModules(): Set<Module> = modules() + parents().flatMap { it.allModules() }

        fun loadArchive(name: String): ResolvedArchive? =
            nameToArchive[name] ?: module.layer.allModules().find { it.name == name }?.let(::ResolvedJpm)

        parents = run {
            module.descriptor.requires()
                .filterNot {
                    it.modifiers().contains(ModuleDescriptor.Requires.Modifier.STATIC)
                }.mapTo(HashSet()) {
                    loadArchive(it.name())!!
                }
        }
    }
}