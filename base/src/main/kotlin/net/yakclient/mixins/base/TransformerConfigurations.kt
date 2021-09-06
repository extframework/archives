package net.yakclient.mixins.base

import net.yakclient.mixins.api.Injection
import net.yakclient.mixins.api.METHOD_SELF
import kotlin.reflect.KClass

object TransformerConfigurations {
    fun mixinOf(from: List<Class<*>>): TransformerConfig =
        TransformerConfig.of {
            for (clazz in from) {
                for (method in clazz.methods) {
                    if (method.isAnnotationPresent(Injection::class.java)) {
                        val injection = method.getAnnotation(Injection::class.java)

                        val source = InstructionAdapters.RemoveNonPositiveOpcodes(InstructionAdapters.RemoveLastReturn(Sources.sourceOf(method)))

                        val type = injection.type
                        val signature =
                            injection.to.takeIf { it != METHOD_SELF } ?: ByteCodeUtils.byteCodeSignature(method)
                        addMt(
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
    //private fun data(cls: Class<*>): Set<MixinMetaData> {
    //        require(cls.isAnnotationPresent(Mixer::class.java)) { "Mixins must be annotated with @Mixer" }
    //        val mixer = cls.getAnnotation(Mixer::class.java)
    //        val mixins = HashSet<MixinMetaData>()
    //        for (method in cls.declaredMethods) {
    //            if (method.isAnnotationPresent(Injection::class.java)) {
    //                val injection = method.getAnnotation(Injection::class.java)
    //                val methodTo = mixinToMethodName(method)
    //                require(
    //                    declaredMethod(
    //                        mixer.value,
    //                        methodTo,
    //                        *method.parameterTypes
    //                    )
    //                ) { "Failed to find a appropriate method to mix to" }
    //                mixins += MixinMetaData(
    //                    cls.name,
    //                    byteCodeSignature(method),
    //                    mixer.value,
    //                    if (injection.to == METHOD_SELF) byteCodeSignature(method) else methodTo,
    //                    injection.type,
    //                    injection.priority
    //                )
    //            }
    //        }
    //        return mixins
    //    }
}