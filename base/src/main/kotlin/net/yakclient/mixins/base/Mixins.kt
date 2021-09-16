package net.yakclient.mixins.base

import net.bytebuddy.agent.ByteBuddyAgent

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.lang.instrument.ClassDefinition
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.ForkJoinTask

/**
 * The base of the mixin api. Provides an easy way to apply transformations to
 * given classes.
 *
 * @since 1.1-SNAPSHOT
 * @author Durgan McBroom
 */
public object Mixins {
    private val pool: ForkJoinPool = ForkJoinPool.commonPool()
    private val instrumentation = ByteBuddyAgent.install()

    /**
     * Applies the given config to the given classes. This operation will be asynchronous. If
     * told to apply non-asynchronously a task will still be submitted however this method
     * will block.
     *
     * @param classes the classes to apply to.
     * @param config the transformations to apply to the given classes.
     * @param async if the task should be blocking or not.
     *
     * @return the submitted task.
     */
    @JvmOverloads
    public fun apply(classes: List<Class<*>>, config: TransformerConfig, async: Boolean = true): ForkJoinTask<*> =
        pool.submit {
            for (clazz in classes) {
                applyInternal(clazz, config)
            }
        }.also { if (async) it.join() }


    /**
     * Applies the given configuration to a single class.
     *
     * @param clazz the class to apply to.
     * @param config the configuration to apply to the given class.
     *
     * @return the submitted task.
     */
    public fun apply(clazz: Class<*>, config: TransformerConfig): ForkJoinTask<*> = apply(listOf(clazz), config)

    /**
     * A convenience method for applying the given config to a
     * single class(kotlin only).
     */
    public inline fun <reified C> apply(config: TransformerConfig): ForkJoinTask<*> = apply(C::class.java, config)


    private fun applyInternal(`class`: Class<*>, config: TransformerConfig) {
        val classReader = ClassReader(`class`.name)

        val writer = ClassWriter(ClassWriter.COMPUTE_FRAMES)
        val resolver = ClassResolver(writer, config)
        classReader.accept(resolver, 0)

        instrumentation.redefineClasses(ClassDefinition(`class`, writer.toByteArray()))
    }
}
