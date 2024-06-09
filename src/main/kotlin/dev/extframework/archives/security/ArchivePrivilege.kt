package dev.extframework.archives.security

public interface ArchivePrivilege {
    public val name: String

    public fun checkAccess(o: Any) : Boolean
}