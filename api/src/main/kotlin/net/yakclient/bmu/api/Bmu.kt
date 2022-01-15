package net.yakclient.bmu.api

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
public object Bmu {
    private val pool: ForkJoinPool = ForkJoinPool.commonPool()
    private val instrumentation = ByteBuddyAgent.install()

    /**
     * Resolves the given class with the given transformer configuration. Returns
     * the byte array of the transformed class. Note, this method does not apply
     * any changes, application should be done with Mixins#apply.
     *
     * @param `class` The class to resolve
     * @param config The configuration to resolve
     * @return The resolved byte array.
     *
     * @since 1.1.4
     */
    public fun resolve(reader: ClassReader, config: TransformerConfig): ByteArray {
        val writer = ClassWriter(ClassWriter.COMPUTE_FRAMES)
        val resolver = ClassResolver(writer, config)
        reader.accept(resolver, 0)

        return writer.toByteArray()
    }

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


    private fun applyInternal(`class`: Class<*>, config: TransformerConfig) =
        instrumentation.redefineClasses(ClassDefinition(`class`, resolve(ClassReader(`class`.name), config)))
}
