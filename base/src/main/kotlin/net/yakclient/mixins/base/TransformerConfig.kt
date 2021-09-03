package net.yakclient.mixins.base

class TransformerConfig(
    val ct: ClassTransformer,
    val mt: MethodTransformer,
    val ft: FieldTransformer
) {
    companion object {
        fun of(block: TransformerConfigScope.() -> Unit): TransformerConfig =
            TransformerConfigScope().also(block)
                .let { config ->
                    fun <T : InjectionTransformer<*>> proxyOrFirst(l: List<T>, proxy: (List<T>) -> T): T =
                        if (l.size == 1) l.first() else proxy(l)

                    TransformerConfig(
                        proxyOrFirst(config.cts) { ProxiedClassTransformer(it) },
                        proxyOrFirst(config.mts) { ProxiedMethodTransformer(it) },
                        proxyOrFirst(config.fts) { ProxiedFieldTransformer(it) }
                    )
                }
    }

    class TransformerConfigScope(
        val cts: MutableList<ClassTransformer> = ArrayList(),
        val mts: MutableList<MethodTransformer> = ArrayList(),
        val fts: MutableList<FieldTransformer> = ArrayList()
    ) {
        fun addCt(t: ClassTransformer) = cts.add(t)
        fun addMt(t: MethodTransformer) = mts.add(t)
        fun addFt(t: FieldTransformer) = fts.add(t)
    }
}