package net.yakclient.mixin.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation marks a field or Method as a Shadow.
 * What this means is that the field/method is provided
 * in the class that will be mixed too, however its implementation
 * here is not provided and needed to pass compilation.
 *
 * an Example;
 *
 * <pre>
 * {@code
 *  ...
 *
 * @Shadow
 *  private int shadow_field;
 *
 *  //Or it can be used as
 *
 * @Shadow
 * private abstract int shadow_method();
 *
 * ...
 * }
 * </pre>
 *
 * @author Durgan McBroom
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Shadow {
}
