package net.yakclient.mixins.base

import net.yakclient.mixins.api.Injection
import net.yakclient.mixins.api.METHOD_SELF
import net.yakclient.mixins.base.TransformerConfig.TransformerConfigScope
import net.yakclient.mixins.base.mixin.Injectors
import net.yakclient.mixins.base.mixin.MixinInjectionTransformer
import kotlin.reflect.KClass

/**
 * Basic predefined TransformerConfigurations that can be created using
 * this utility class.
 *
 * @see TransformerConfig
 *
 * @since 1.1-SNAPSHOT
 * @author Durgan McBroom
 */
public object TransformerConfigurations {
    /**
     * Given a class annotated with @Mixer creates a configuration scope with
     * all basic mixin transformers. This can be added to.
     *
     * @since 1.1-SNAPSHOT
     * @author Durgan McBroom
     */
    public fun mixinOf(clazz: Class<*>): TransformerConfigScope =
        TransformerConfigScope().of {
            for (method in clazz.methods) {
                if (method.isAnnotationPresent(Injection::class.java)) {
                    val injection = method.getAnnotation(Injection::class.java)

                    val source = Sources.sourceOf(method)

                    val type = injection.type
                    val signature =
                        injection.to.takeIf { it != METHOD_SELF } ?: ByteCodeUtils.byteCodeSignature(method)

                    add(
                        TargetedMethodTransformer(
                            MixinInjectionTransformer(Injectors.of(type), type, source),
                            ByteCodeUtils.MethodSignature.of(signature)
                        )
                    )
                }
            }
        }

    /**
     * Creates a mixin configuration from a KClass.
     */
    public fun mixinOf(from: KClass<*>) : TransformerConfigScope = mixinOf(from.java)
}