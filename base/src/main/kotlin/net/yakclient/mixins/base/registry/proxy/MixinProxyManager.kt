package net.yakclient.mixins.base.registry.proxy

import java.util.*

typealias FunctionalProxy = MixinProxySettings.() -> Unit

data class MixinProxySettings(
    var cancel: Boolean = false,
)


object MixinProxyManager {
    private val proxys: MutableMap<UUID, FunctionalProxy>

    init {
        proxys = HashMap()
    }

    fun proxy(uuid: UUID): MixinProxySettings {
        val proxy = requireNotNull(this.proxys[uuid]) { "Invalid UUID, no proxy found" }
        return MixinProxySettings().also { proxy.invoke(it) }
    }

    fun registerProxy(proxy: FunctionalProxy): UUID {
        val uuid = UUID.randomUUID()
        check(proxys.containsKey(uuid)) { "Cannot override existing proxy's. Your UUID's have matched!" }
        return uuid.also { this.proxys[it] = proxy }
    }
}