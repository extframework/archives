package net.yakclient.archives.impl.zip

import net.yakclient.archives.ArchiveFinder
import java.nio.file.Path
import java.util.jar.JarFile
import java.util.zip.ZipFile
import kotlin.reflect.KClass

internal class ZipFinder : ArchiveFinder<ZipHandle> {
    override val type: KClass<ZipHandle> = ZipHandle::class

    override fun find(path: Path): ZipHandle {
        return ZipHandle(
            JarFile(
                path.toFile().also { assert(it.exists()) },
                true,
                ZipFile.OPEN_READ,
                JarFile.runtimeVersion()
            ), path.toUri()
        )
    }
}