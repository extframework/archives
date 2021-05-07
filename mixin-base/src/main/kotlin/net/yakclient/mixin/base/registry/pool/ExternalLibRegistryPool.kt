package net.yakclient.mixin.base.registry.pool

import net.yakclient.mixin.base.internal.loader.PackageTarget
import java.net.URL

class ExternalLibRegistryPool : RegistryPool<URL>() {
    override fun pool(type: URL): Location {
        val key = ExternalLibLocation(type)
        if (!pool.containsKey(key)) pool[key] = PoolQueue()
        pool[key]!!.add(type)
        return key
    }

    override fun register(location: Location): PackageTarget {
        throw ClassNotFoundException()
    }
}