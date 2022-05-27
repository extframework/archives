package net.yakclient.archives.security

import net.yakclient.archives.internal.security.ArchivePermissionPrivilege
import java.security.Permission

public fun Permission.toPrivilege() : ArchivePrivilege = ArchivePermissionPrivilege(this)