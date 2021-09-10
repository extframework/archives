package net.yakclient.mixins.base

class TransformerConfig(
    val ct: ClassTransformer,
    val mt: MethodTransformer,
    val ft: FieldTransformer
) {
    class TransformerConfigScope(
        private val cts: MutableList<ClassTransformer> = ArrayList(),
        private val mts: MutableList<MethodTransformer> = ArrayList(),
        private val fts: MutableList<FieldTransformer> = ArrayList()
    ) {
        fun add(t: ClassTransformer) = cts.add(t)
        fun add(t: MethodTransformer) = mts.add(t)
        fun add(t: FieldTransformer) = fts.add(t)

        fun of(block: TransformerConfigScope.() -> Unit): TransformerConfigScope = apply(block)

        fun build(): TransformerConfig = let { config ->
            fun <T : InjectionTransformer<*>> proxyOrFirst(l: List<T>, proxy: (List<T>) -> T): T =
                if (l.size == 1) l.first() else proxy(l)

            TransformerConfig(
                proxyOrFirst(config.cts) { ProxiedClassTransformer(it) },
                proxyOrFirst(config.mts) { ProxiedMethodTransformer(it) },
                proxyOrFirst(config.fts) { ProxiedFieldTransformer(it) }
            )
        }
    }
}