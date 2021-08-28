package net.yakclient.mixins.api

/**
 * `@Injection` marks a method as a Injection, who's Java
 * bytecode will be injected into the reciprocal.
 *
 * This annotation is meant to be very simple and easy to use.
 * In fact nothing has to be pre-defined and values will
 * default to acceptable options.
 *
 * A basic example, in this case all we have to provide is
 * the annotation;
 *
 * <pre>
 * `...
 *
 * public void myMethod(Integer... params) { ... }
 * ...
` *
</pre> *
 *
 * In a more complex example we could provide as many
 * options as we would like;
 *
 * <pre>
 * `...
 * = "a_different_name", type = InjectionType.BEFORE_INVOKE, priority = Priority.LOW)
 * public void myMethod(Integer... params) { ... }
 * ...
` *
</pre> *
 *
 * @see InjectionType
 *
 * @see Priority
 *
 *
 * @author Durgan McBroom
 */

const val METHOD_SELF: String = "<SELF>"

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class Injection(
    /**
     * The `Injection#to()` method defines the method that
     * will be injected into. If this value is not provided it will
     * default to the name of the method that called.
     *
     * @return the method that will be mixed into.
     */
    val to: String = METHOD_SELF,
    /**
     * The `Injection#type()` method specifies the type
     * of injection that will be performed. This will default
     * to `InjectionType.AFTER_BEGIN`
     *
     * @see InjectionType
     *
     *
     * @return the type of injection that will be performed.
     */
    val type: Int = InjectionType.AFTER_BEGIN,
    /**
     * Specifies a injection priority that will in the end determine
     * what different injections will go first if there is a
     * collision.
     *
     * @return the priority of this injection.
     */
    val priority: Int = Priority.MEDIUM
)