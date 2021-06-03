package net.yakclient.mixin.base.registry.proxy

import net.yakclient.mixin.base.registry.FunctionalProxy
import net.yakclient.mixin.base.registry.FunctionalProxy.ProxyResponseData
import org.jetbrains.annotations.Contract
import java.util.*

object MixinProxyManager {
    private val proxys: MutableMap<UUID, FunctionalProxy>

    init {
        proxys = HashMap()
    }

    @Contract(pure = true)
    fun proxy(uuid: UUID): ProxyResponseData {
        require(this.proxys.containsKey(uuid)) { "Invalid UUID, no proxy found" }
        return FunctionalProxy.run(this.proxys[uuid]!!)
    }

    fun registerProxy(proxy: FunctionalProxy): UUID {
        val uuid = UUID.randomUUID();
        require(this.proxys.containsKey(uuid) && this.proxys[uuid] != null) { "Cannot override existing proxy's. Your UUID's have matched!" }
        this.proxys[uuid] = proxy
        return uuid
    }
}