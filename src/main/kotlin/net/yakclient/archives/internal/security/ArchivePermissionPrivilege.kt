package net.yakclient.archives.internal.security

import net.yakclient.archives.security.ArchivePrivilege
import net.yakclient.common.util.runCatching
import java.security.Permission

internal data class ArchivePermissionPrivilege(
    val permission: Permission
) : ArchivePrivilege {
    override val name: String = permission.name

    override fun checkAccess(o: Any): Boolean =
        runCatching(SecurityException::class) { permission.checkGuard(o) } != null
}