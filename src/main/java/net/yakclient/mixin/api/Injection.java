package net.yakclient.mixin.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@code @Injection} marks a method as a Injection, who's Java
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
 * {@code
 *  ...
 * @Injection
 *     public void myMethod(Integer... params) { ... }
 *  ...
 * }
 * </pre>
 *
 * In a more complex example we could provide as many
 * options as we would like;
 *
 * <pre>
 * {@code
 *  ...
 *     @Injection(to = "a_different_name", type = InjectionType.BEFORE_INVOKE, priority = Priority.LOW)
 *     public void myMethod(Integer... params) { ... }
 *  ...
 * }
 * </pre>
 *
 * @see InjectionType
 * @see Priority
 *
 * @author Durgan McBroom
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Injection {
    String METHOD_SELF = "<SELF>";

    /**
     * The {@code Injection#to()} method defines the method that
     * will be injected into. If this value is not provided it will
     * default to the name of the method that called.
     *
     * @return the method that will be mixed into.
     */
    String to() default METHOD_SELF;

    /**
     * The {@code Injection#type()} method specifies the type
     * of injection that will be performed. This will default
     * to {@code InjectionType.AFTER_BEGIN}
     *
     * @see InjectionType
     *
     * @return the type of injection that will be performed.
     */
    InjectionType type() default InjectionType.AFTER_BEGIN;

    /**
     * Specifies a injection priority that will in the end determine
     * what different injections will go first if there is a
     * collision.
     *
     * @return the priority of this injection.
     */
    int priority() default Priority.MEDIUM;
}
