package net.yakclient.mixin.base.registry.pool

import net.yakclient.mixin.base.internal.loader.ContextPoolManager
import net.yakclient.mixin.base.registry.proxy.MixinProxyManager
import net.yakclient.mixin.base.target.JarTarget
import net.yakclient.mixin.base.target.Target
import java.net.URL

class ExternalLibRegistryPool : RegistryPool<URL>() {
    override fun pool(type: URL): Location {
        return ExternalLibLocation(type).also { loc ->
            (if (!pool.containsKey(loc)) PoolQueue<URL>().also { pool[loc] = it } else pool[loc]!!).add(type)
            if (!pool.containsKey(loc)) pool[loc] = PoolQueue<URL>().apply { add(type) }
        }
    }

    override fun mix(location: Location) {
        require(location is ExternalLibLocation) { "Given location must be a external library to register with the lib registry pool!" }
        ContextPoolManager.libPool.addTarget(JarTarget(location.url))
    }
}