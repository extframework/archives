package dev.extframework.archives.internal.security

import dev.extframework.archives.security.ArchivePrivilege
import dev.extframework.common.util.runCatching
import java.security.Permission

internal data class ArchivePermissionPrivilege(
    val permission: Permission
) : ArchivePrivilege {
    override val name: String = permission.name

    override fun checkAccess(o: Any): Boolean =
        runCatching(SecurityException::class) { permission.checkGuard(o) } != null
}