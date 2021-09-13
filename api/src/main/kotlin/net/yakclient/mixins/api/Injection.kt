package net.yakclient.mixins.api

/**
 * `@Injection` marks a method as an Injection. In a mixin this will be
 * code that is injected into another spot.
 *
 * This annotation is meant to be very simple and easy to use.
 * In fact nothing has to be pre-defined and values will
 * default to acceptable options.
 *
 * The most basic example is as follows:
 * ```
 * @Injection
 * fun doSomething(int: Int, any: Any) : String { /*Injection*/ }
 * ```
 * This configuration will inject the body of the annotated method into
 * the methods with the signature `doSomething(ILkotlin/Any;)Ljava/lang/String`
 *
 * You can of course change options to something more like this:
 *
 * ```
 * @Injection(
 *    to="doSomething(ILkotlin/Any;)",
 *    type = InjectionType.AFTER_BEGIN,
 *    priority = Priority.MEDIUM)
 * fun asdf(int: Int, any: Any) /* Can have a void return type where the mixin does not exit the method */ { /*Injection*/ }
 * ```
 *
 * This will inject to a method with the signature `doSomething(ILkotlin/Any;)`(ignoring return type) with a
 * injection type of [AFTER_BEGIN][InjectionType.AFTER_BEGIN] and a [medium priority][Priority.MEDIUM]
 *
 * @see InjectionType
 * @see Priority
 *
 * @author Durgan McBroom
 */

public const val METHOD_SELF: String = "<SELF>"

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
public annotation class Injection(
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
     * @see Priority
     *
     * @return the priority of this injection.
     */
    val priority: Int = Priority.MEDIUM
)