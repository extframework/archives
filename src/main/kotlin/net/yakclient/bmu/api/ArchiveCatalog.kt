package net.yakclient.bmu.api

import java.util.*
import kotlin.reflect.KClass

public object ArchiveCatalog {
    private val _archives: MutableSet<ResolvedArchive> = HashSet()
    public val archives: Set<ResolvedArchive>
        get() = _archives.toSet()

    internal fun catalog(archive: ResolvedArchive) = _archives.add(archive)

    public fun <T : Any> loadService(service: KClass<T>): List<T> = loadService(service.java)

    public fun <T : Any> loadService(service: Class<T>): List<T> =
        ServiceLoader.load(service).toList() + archives.flatMap { it.loadService(service.name) }
            .map { it.getConstructor().newInstance() }
            .map { it as T }
}