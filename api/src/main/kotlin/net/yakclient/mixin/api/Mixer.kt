package net.yakclient.mixin.api

/**
 * `Mixer` marks a class as a Mixer. This is a alternate
 * for `IMixer` but should be used preferably.
 *
 * An example;
 *
 * <pre>
 * `public abstract class ExampleClass { ... }
` *
</pre> *
 *
 * @author Durgan McBroom
 */
const val RESOLVE_MODULE = "<RESOLVE_MODULE>"

@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
annotation class Mixer(
    /**
     * `Mixer#type()` provides the class that will
     * be mixed to.
     *
     * @return returns the class that will be mixed into.
     */
    val value: String,

    val module: String = RESOLVE_MODULE
)