package net.yakclient.mixins.base

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.yakclient.mixins.base.agent.YakMixinsAgent
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.lang.instrument.ClassDefinition

object Mixins {
    fun apply(classes: List<Class<*>>, config: TransformerConfig) = runBlocking {
        for (`class` in classes) {
            launch { applyInternal(`class`, config) }
        }
    }



    private fun applyInternal(`class`: Class<*>, config: TransformerConfig) {
        val classReader = ClassReader(`class`.name)

        val writer = ClassWriter(ClassWriter.COMPUTE_FRAMES)

        val resolver = ClassResolver(writer, config)
        classReader.accept(resolver, 0)

        YakMixinsAgent.instrumentation.redefineClasses(ClassDefinition(`class`, writer.toByteArray()))
    }
}