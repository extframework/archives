package net.yakclient.mixin.base.target

import net.yakclient.mixin.base.internal.loader.ExternalJarLoader
import net.yakclient.mixin.base.internal.loader.ProxyClassLoader
import net.yakclient.mixin.base.internal.loader.TargetClassLoader
import java.net.URL

data class JarTarget(private val url: URL) : Target {
    override fun createLoader(parent: ClassLoader): ProxyClassLoader {
        return ExternalJarLoader(this.url, parent)
    }

    override fun isTargetOf(target: Target): Boolean {
        return this.equals(target)
    }

    override fun name(): String {
        return this.url.file
    }
}