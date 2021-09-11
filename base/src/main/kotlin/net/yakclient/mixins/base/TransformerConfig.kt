package net.yakclient.mixins.base

/**
 * The base config for transforming classes. Will always contain only 1 ClassTransformer,
 * MethodTransformer, and FieldTransformer. This class is mostly used to configure
 * InjectionResolvers.
 *
 * @see InjectionTransformer
 * @see InjectionResolver
 *
 * @constructor Creates a config with the provided transformers.
 * @since 1.1-SNAPSHOT
 *
 * @author Durgan McBroom
 */
class TransformerConfig(
    val ct: ClassTransformer,
    val mt: MethodTransformer,
    val ft: FieldTransformer
) {
    /**
     * The TransformerConfig builder scope which provides utilities for creating a
     * transformer config. The transformer scope can accept multiple InjectionTransformers
     * and will create a proxy if more than one is collected.
     *
     * @constructor Constructs a scope with the transformers provided, or none if not specified.
     * @since 1.1-SNAPSHOT
     *
     * @see ProxiedTransformer
     *
     * @author Durgan McBroom
     */
    class TransformerConfigScope(
        private val cts: MutableList<ClassTransformer> = ArrayList(),
        private val mts: MutableList<MethodTransformer> = ArrayList(),
        private val fts: MutableList<FieldTransformer> = ArrayList()
    ) {
        /**
         * Adds a ClassTransformer.
         *
         * @param t the transformer to add.
         */
        fun add(t: ClassTransformer) = cts.add(t)

        /**
         * Adds a MethodTransformer.
         *
         * @param t the transformer to add.
         */
        fun add(t: MethodTransformer) = mts.add(t)

        /**
         * Adds a FieldTransformer.
         *
         * @param t the Transformer to add.
         */
        fun add(t: FieldTransformer) = fts.add(t)

        /**
         * Applies the given expression with 'this' as the scope.
         *
         * @param block the expression to evaluate.
         */
        fun of(block: TransformerConfigScope.() -> Unit): TransformerConfigScope = apply(block)

        /**
         * Constructs a TransformerConfig from the collected transformers. If more
         * than one transformer is found a ProxiedTransformer will be created instead.
         *
         * @return The built TransformerConfig.
         */
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