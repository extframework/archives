@file:JvmName("ZipArchives")

package dev.extframework.archives.zip

import dev.extframework.archives.ArchiveHandle

public fun classLoaderToArchive(classloader: ClassLoader): ArchiveHandle = ZipHandle(
    classloader,
    setOf(),
    classloader.parent?.let { setOf(classLoaderToArchive(it)) } ?: setOf()
)
