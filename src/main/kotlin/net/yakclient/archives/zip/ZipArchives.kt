@file:JvmName("ZipArchives")

package net.yakclient.archives.zip

import net.yakclient.archives.ArchiveHandle

public fun classLoaderToArchive(classloader: ClassLoader): ArchiveHandle = ZipHandle(
    classloader,
    classloader.definedPackages.mapTo(HashSet(), Package::getName),
    classloader.parent?.let { setOf(classLoaderToArchive(it)) } ?: setOf()
)
