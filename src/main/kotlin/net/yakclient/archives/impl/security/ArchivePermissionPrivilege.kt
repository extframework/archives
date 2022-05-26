package net.yakclient.archives.impl.security

import net.yakclient.archives.security.ArchivePrivilege
import net.yakclient.common.util.runCatching
import java.security.Permission

public data class ArchivePermissionPrivilege(
    public val permission: Permission
) : ArchivePrivilege {
    override val name: String = permission.name

    override fun checkAccess(o: Any): Boolean =
        runCatching(SecurityException::class) { permission.checkGuard(o) } != null
}