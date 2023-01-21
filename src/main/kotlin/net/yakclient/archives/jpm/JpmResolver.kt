package net.yakclient.archives.jpm

import net.yakclient.archives.ArchiveResolver
import net.yakclient.archives.ClassLoaderProvider
import net.yakclient.archives.ArchiveHandle
import net.yakclient.common.util.LazyMap
import java.io.FileOutputStream
import java.lang.module.Configuration
import java.lang.module.ModuleDescriptor
import java.lang.module.ModuleFinder
import java.lang.module.ModuleReference
import java.lang.module.ResolvedModule
import java.nio.file.Files
import java.util.*
import java.util.jar.JarEntry
import java.util.jar.JarOutputStream
import kotlin.reflect.KClass

internal class JpmResolver : ArchiveResolver<JpmReference, JpmResolutionResult> {
    override val type: KClass<JpmReference> = JpmReference::class

    override fun resolve(
        archiveRefs: List<JpmReference>,
        clProvider: ClassLoaderProvider<JpmReference>,
        parents: Set<ArchiveHandle>,
    ): List<JpmResolutionResult> {
        val refs = archiveRefs.map(::loadRef)
        val refsByName = refs.associateBy { it.descriptor().name() }

        for (ref in refs)
            ref.descriptor().requires()
                .filterNot { it.modifiers().contains(ModuleDescriptor.Requires.Modifier.STATIC) }
                .forEach { r ->
                    fun Configuration.provides(name: String): Boolean =
                        modules().any { it.name() == name } || parents().any { it.provides(name) }

                    val dependenciesPresent = parents.filterIsInstance<JpmHandle>().any {
                        it.module.name == r.name() || it.configuration.provides(r.name())
                    } || ModuleLayer.boot().configuration().provides(r.name()) || refs.any {
                        it.descriptor().name() == r.name()
                    }

                    if (!dependenciesPresent) throw IllegalArgumentException("Dependency: '${r.name()}' Required by '${ref.name}' but not found in parents!")
                }

        val loaders = LazyMap<String, ClassLoader> {
            clProvider(
                refsByName[it] ?: throw IllegalStateException(
                    "Error occurred when trying to create a class loader for module $it because module $it is not recognized. Only suppose to load modules for ${
                        refs.joinToString(
                            prefix = "[",
                            postfix = "]"
                        ) { m -> m.descriptor().name() }
                    }"
                ),
            )
        }

        // Mapping to a HashSet to avoid multiple configuration that are the same(they do not override equals and hashcode but using the object ID's should be good enough)
        val parentLayers = parents
            .filterIsInstance<JpmHandle>()
            .mapTo(HashSet()) { it.layer }

        val finder = object : ModuleFinder {
            override fun find(name: String): Optional<ModuleReference> = Optional.ofNullable(refsByName[name])

            override fun findAll(): MutableSet<ModuleReference> = refsByName.values.toMutableSet()
        }

        val configuration = Configuration.resolveAndBind(
            finder,
            parentLayers.map { it.configuration() } + ModuleLayer.boot().configuration(),
            ModuleFinder.of(),
            finder.findAll().map(ModuleReference::descriptor).map(ModuleDescriptor::name)
        )

        val controller = ModuleLayer.defineModules(
            configuration,
            parentLayers.toList() + ModuleLayer.boot(),
            loaders::get
        )

        val layer = controller.layer()

        return layer.modules().map(::JpmHandle).map { JpmResolutionResult(it, controller, it.module) }
    }

    private fun loadRef(ref: JpmReference): JpmReference = if (!ref.modified) ref else {
        val desc = ref.descriptor()
        val archiveName = "${desc.name().replace('.', '-')}${
            desc.rawVersion().map { "-$it" }.orElse("")
        }"

        // Why do I have to use the '.'?????
        val jar = Files.createTempFile(archiveName, ".jar")

        JarOutputStream(FileOutputStream(jar.toFile())).use { target ->
            ref.reader.entries().forEach { e ->
                val entry = JarEntry(e.name)

                target.putNextEntry(entry)

                val eIn = e.resource.open()

                //Stolen from https://stackoverflow.com/questions/1281229/how-to-use-jaroutputstream-to-create-a-jar-file
                val buffer = ByteArray(1024)

                while (true) {
                    val count: Int = eIn.read(buffer)
                    if (count == -1) break

                    target.write(buffer, 0, count)
                }

                target.closeEntry()
            }
        }

        assert(Files.exists(jar)) { "Failed to write jar to temp directory!" }

        JpmReference(ModuleFinder.of(jar).find(ref.descriptor().name())
            .orElseThrow { IllegalArgumentException("Archive reference that should be present is not! Path: $jar") })
    }
}