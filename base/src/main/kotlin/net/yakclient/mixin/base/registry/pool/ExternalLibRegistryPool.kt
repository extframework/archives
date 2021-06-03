package net.yakclient.mixin.base.registry.pool

import net.yakclient.mixin.base.internal.loader.ContextPoolManager
import net.yakclient.mixin.base.target.JarTarget
import net.yakclient.mixin.base.target.Target
import java.net.URL

class ExternalLibRegistryPool : RegistryPool<URL>() {
    override fun pool(type: URL): Location {
        val key = ExternalLibLocation(type)
        if (!pool.containsKey(key)) pool[key] = PoolQueue()
        pool[key]!!.add(type)
        return key
    }

    override fun register(location: Location): Target {
        require(location is ExternalLibLocation) { "Given location must be a external library to register with the lib registry pool!" }
        val target = JarTarget(location.url)
        ContextPoolManager.applyTarget(target)

        return target;
    }
}