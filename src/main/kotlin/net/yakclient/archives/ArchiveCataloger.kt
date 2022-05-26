package net.yakclient.archives

import java.util.*
import kotlin.reflect.KClass

//public object ArchiveCataloger {
//    private val catalogs: MutableMap<ResolvedArchive, ArchiveCatalog> = HashMap()
//
//    internal fun catalog(archive: ResolvedArchive) {
//        catalogs[archive] = ArchiveCatalog(archive, HashSet())
//    }
//
//    internal fun updateDescendant(archive: ResolvedArchive, descendant: ResolvedArchive) {
//        (catalogs[archive]
//            ?: throw IllegalStateException("Archive: $archive must be cataloged first!"))._descendants.add(descendant)
//    }
//
//    public fun <T : Any> loadService(service: KClass<T>, archive: ResolvedArchive): List<T> =
//        loadService(service.java, archive)
//
//    public fun <T : Any> loadService(service: Class<T>, archive: ResolvedArchive): List<T> {
//        this::class.java.module.addUses(service)
//
//
////
////        return ServiceLoader.load(service).toList() + archives.flatMap { it.loadService(service.name) }
////            .map { it.getConstructor().newInstance() }
////            .map { it as T }
//    }
//}
//
//public data class ArchiveCatalog(
//    public val archive: ResolvedArchive,
//    internal val _descendants: MutableSet<ResolvedArchive>
//) {
//    public val descendants: Set<ResolvedArchive>
//        get() = _descendants.toSet()
//}