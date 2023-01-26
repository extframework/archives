package net.yakclient.archives.mixin

import net.yakclient.archives.transform.TransformerConfig
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.FieldNode

public object FieldInjection : MixinInjection<FieldInjectionData> {
    override fun apply(
        data: FieldInjectionData
    ): TransformerConfig.Mutable = TransformerConfig.of {
        transformClass {
            it.apply {
                val node = FieldNode(
                    data.access,
                    data.name,
                    data.type,
                    data.signature,
                    data.initialValue
                )

                it.fields.add(node)
            }
        }
    }
}

public data class FieldInjectionData(
    val access: Int = Opcodes.ACC_PUBLIC,
    val name: String,
    val type: String,
    val signature: String? = null,
    val initialValue: Any? = null, // Must be a primitive
) : MixinInjection.InjectionData