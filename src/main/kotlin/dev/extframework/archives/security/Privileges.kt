package dev.extframework.archives.security

import dev.extframework.archives.internal.security.ArchivePermissionPrivilege
import java.security.Permission

public fun Permission.toPrivilege() : ArchivePrivilege = ArchivePermissionPrivilege(this)