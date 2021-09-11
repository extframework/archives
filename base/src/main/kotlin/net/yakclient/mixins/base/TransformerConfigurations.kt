package net.yakclient.mixins.base

import net.yakclient.mixins.api.Injection
import net.yakclient.mixins.api.METHOD_SELF
import net.yakclient.mixins.base.mixin.Injectors
import net.yakclient.mixins.base.mixin.MixinInjectionTransformer
import kotlin.reflect.KClass

object TransformerConfigurations {
    fun mixinOf(from: List<Class<*>>): TransformerConfig.TransformerConfigScope =
        TransformerConfig.TransformerConfigScope().of {
            for (clazz in from) {
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
                                signature
                            )
                        )
                    }
                }
            }
        }

    fun mixinOf(from: Class<*>) = mixinOf(listOf(from))

    fun mixinOf(from: KClass<*>) = mixinOf(from.java)
}