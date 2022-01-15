package net.yakclient.bmu.api.mixin

import net.yakclient.bmu.api.*
import net.yakclient.bmu.api.mixin.annotations.Injection
import net.yakclient.bmu.api.mixin.annotations.METHOD_SELF
import net.yakclient.bmu.api.mixin.annotations.Mixer
import kotlin.reflect.KClass

/**
 *
 */
public object Mixins {
    /**
     * Given a class annotated with @Mixer creates a configuration scope with
     * all basic mixin transformers. This can be added to.
     *
     * @since 1.1-SNAPSHOT
     * @author Durgan McBroom
     */
    public fun mixinOf(clazz: Class<*>): TransformerConfig.MutableTransformerConfiguration =
        TransformerConfig.of {
            val mixer = clazz.getAnnotation(Mixer::class.java)
                ?: throw IllegalArgumentException("Given class must be annotated with @Mixer")
            for (method in clazz.methods) {
                if (method.isAnnotationPresent(Injection::class.java)) {
                    val injection = method.getAnnotation(Injection::class.java)

                    val source = InstructionAdapters.AlterThisReference(
                        Sources.sourceOf(method),
                        mixer.value.replace('.', '/'),
                        clazz.name.replace('.', '/')
                    )

                    val type = injection.type
                    val signature =
                        injection.to.takeIf { it != METHOD_SELF } ?: ByteCodeUtils.byteCodeSignature(method)

                    transformMethod(
                        TargetedMethodTransformer(
                            ByteCodeUtils.MethodSignature.of(signature),
                            MixinInjectionTransformer(Injectors.of(type), type, source)
                        )
                    )
                }
            }
        }

    /**
     * Creates a mixin configuration from a KClass.
     */
    public fun mixinOf(from: KClass<*>): TransformerConfig.MutableTransformerConfiguration = mixinOf(from.java)
}