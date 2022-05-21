package net.yakclient.bmu.api.mixin

import net.yakclient.bmu.api.transform.*

public object Mixins {
    public data class InjectionMetaData(
        public val resolver: InstructionResolver,
        public val to: String,
        public val type: InjectionType = InjectionType.AFTER_BEGIN,
        )
    /**
     * Given a class annotated with @Mixer creates a configuration scope with
     * all basic mixin transformers. This can be added to.
     *
     * @since 1.1-SNAPSHOT
     * @author Durgan McBroom
     */
    public fun mixinOf(to: String, self: String, injections: List<InjectionMetaData>): TransformerConfig.MutableTransformerConfiguration = TransformerConfig.of {
        for (injection in injections) {
                val source = InstructionAdapters.AlterThisReference(
                   injection.resolver,
                    to.replace('.', '/'),
                    self.replace('.', '/')
                )

                val type = injection.type
                val signature = injection.to

                transformMethod(
                    TargetedMethodTransformer(
                        MethodSignature.of(signature),
                        MixinInjectionTransformer(Injectors.of(type), -1, source)
                    )
                )
            }
    }

//    /**
//     * Creates a mixin configuration from a KClass.
//     */
//    public fun mixinOf(from: KClass<*>): TransformerConfig.MutableTransformerConfiguration =
//        mixinOf(from.java)
}