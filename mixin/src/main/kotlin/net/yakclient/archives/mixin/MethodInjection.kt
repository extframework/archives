package net.yakclient.archives.mixin

import net.yakclient.archives.transform.InstructionResolver
import net.yakclient.archives.transform.TransformerConfig
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.MethodNode

public object MethodInjection : MixinInjection<MethodInjectionData> {
    override fun apply(
        data: MethodInjectionData
    ): TransformerConfig.MutableTransformerConfiguration = TransformerConfig.of {
        transformClass {
            it.apply {
                val node = MethodNode(
                    data.access,
                    data.name,
                    data.desc,
                    data.signature,
                    data.exceptions.toTypedArray()
                )

                val source = AlterThisReference(
                    data.insnResolver,
                    data.classTo.replace('.', '/'),
                    data.classSelf.replace('.', '/')
                )

                node.instructions = source.get()

                it.methods.add(
                    node
                )
            }
        }
    }
}

public data class MethodInjectionData(
    val classTo: String,
    val classSelf: String,
    val insnResolver: InstructionResolver,

    val access: Int = Opcodes.ACC_PUBLIC,
    val name: String,
    val desc: String,
    val signature: String? = null,
    val exceptions: List<String> = ArrayList()
) : MixinInjection.InjectionData