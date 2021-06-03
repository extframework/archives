package net.yakclient.mixin.base.registry

fun interface FunctionalProxy {
    fun accept(cancel: Runnable)

    class ProxyResponseData(val cancel: Boolean)

    companion object {
        fun run(proxy: FunctionalProxy): ProxyResponseData {
            var cancel = false

            proxy.accept { cancel = true }
            return ProxyResponseData(cancel);
        }
    }
}