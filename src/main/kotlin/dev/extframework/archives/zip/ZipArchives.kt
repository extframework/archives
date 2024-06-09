@file:JvmName("ZipArchives")

package dev.extframework.archives.zip

import dev.extframework.archives.ArchiveHandle

public fun classLoaderToArchive(classloader: ClassLoader): ArchiveHandle = ZipHandle(
    classloader,
    classloader.definedPackages.mapTo(HashSet(), Package::getName),
    classloader.parent?.let { setOf(classLoaderToArchive(it)) } ?: setOf()
)
