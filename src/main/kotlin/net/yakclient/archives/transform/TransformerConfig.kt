package net.yakclient.archives.transform

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
public open class TransformerConfig(
    public val ct: ClassTransformer,
    public val mt: MethodTransformer,
    public val ft: FieldTransformer
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
    public class Mutable(
        private val cts: MutableList<ClassTransformer> = ArrayList(),
        private val mts: MutableList<MethodTransformer> = ArrayList(),
        private val fts: MutableList<FieldTransformer> = ArrayList()
    ) : TransformerConfig(ClassTransformer { node ->
        cts.fold(node) { acc, it -> it(acc) }
    }, MethodTransformer { node -> mts.fold(node) { acc, it -> it(acc) } }, FieldTransformer { node ->
        fts.fold(node) { acc, it -> it(acc) }
    }) {
        /**
         * Adds a ClassTransformer.
         *
         * @param t the transformer to add.
         */
        public fun transformClass(t: ClassTransformer): Boolean = cts.add(t)

        /**
         * Adds a MethodTransformer.
         *
         * @param t the transformer to add.
         */
        public fun transformMethod(t: MethodTransformer): Boolean = mts.add(t)

        /**
         * Adds a FieldTransformer.
         *
         * @param t the Transformer to add.
         */
        public fun transformField(t: FieldTransformer): Boolean = fts.add(t)

        public fun transformClass(name: String, t: ClassTransformer): Boolean =
            cts.add(TargetedClassTransformer(name, t))

        public fun transformMethod(name: String, t: MethodTransformer): Boolean =
            mts.add(TargetedMethodTransformer(MethodSignature.of(name), t))

        public fun transformMethod(signature: MethodSignature, t: MethodTransformer): Boolean =
            mts.add(TargetedMethodTransformer(signature, t))

        public fun transformField(name: String, t: FieldTransformer): Boolean =
            fts.add(TargetedFieldTransformer(name, t))
    }

    public companion object {
        /**
         * Applies and returns a Mutable transformer config to the given block.
         *
         * @param block the block to apply
         * @return the created configuration
         */
        @JvmStatic
        public fun of(block: Mutable.() -> Unit): Mutable =
            Mutable().apply(block)

        public operator fun TransformerConfig.plus(other: TransformerConfig): Mutable {
            return Mutable(
                mutableListOf(this.ct, other.ct),
                mutableListOf(this.mt, other.mt),
                mutableListOf(this.ft, other.ft)
            )
        }
    }
}

