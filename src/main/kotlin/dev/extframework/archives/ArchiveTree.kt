package dev.extframework.archives

import java.io.InputStream

public interface ArchiveTree {
    public fun getResource(name: String) : InputStream?
}