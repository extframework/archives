package net.yakclient.bmu.api.internal.jpm

import net.yakclient.bmu.api.ResolvedArchive
import java.lang.module.Configuration
import java.lang.module.ModuleDescriptor

internal class ResolvedJpm(
    val module: Module,
) : ResolvedArchive {
    override val classloader: ClassLoader=module.classLoader
    override val packages: Set<String> = module.packages
    override val parents: List<ResolvedArchive>
    val configuration: Configuration = module.layer.configuration()
    val layer: ModuleLayer = module.layer
    private val services: Map<String, List<Class<*>>> by lazy {
        module.descriptor.provides().associate { it.service() to it.providers().map(classloader::loadClass) }
    }

    init {
        nameToArchive[module.name] = this

        fun ModuleLayer.allModules(): Set<Module> = modules() + parents().flatMap { it.allModules() }

        fun loadArchive(name: String): ResolvedArchive? =
            nameToArchive[name] ?: module.layer.allModules().find { it.name == name }?.let(::ResolvedJpm)

        parents = run {
            module.descriptor.requires()
                .filterNot {
                    it.modifiers().contains(ModuleDescriptor.Requires.Modifier.STATIC)
                }.map {
                    loadArchive(it.name())!!
                }
        }
    }

    private companion object {
        private val nameToArchive: MutableMap<String, ResolvedArchive> = HashMap()
    }

    override fun loadService(name: String): List<Class<*>> = services[name] ?: ArrayList()
}