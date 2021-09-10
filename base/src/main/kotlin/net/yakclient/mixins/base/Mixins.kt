package net.yakclient.mixins.base

import net.yakclient.mixins.base.agent.YakMixinsAgent
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.lang.instrument.ClassDefinition
import java.util.concurrent.ForkJoinPool

object Mixins {
    private val pool: ForkJoinPool = ForkJoinPool.commonPool()

    fun apply(classes: List<Class<*>>, config: TransformerConfig) = pool.submit {
        for (clazz in classes) {
            applyInternal(clazz, config)
        }
    }

    fun apply(clazz: Class<*>, config: TransformerConfig) = apply(listOf(clazz), config)

    inline fun <reified C> apply(config: TransformerConfig) = apply(C::class.java, config)


    private fun applyInternal(`class`: Class<*>, config: TransformerConfig) {
        val classReader = ClassReader(`class`.name)

        val writer = ClassWriter(ClassWriter.COMPUTE_FRAMES)

        val resolver = ClassResolver(writer, config)
        classReader.accept(resolver, 0)

        YakMixinsAgent.instrumentation.redefineClasses(ClassDefinition(`class`, writer.toByteArray()))
    }
}
