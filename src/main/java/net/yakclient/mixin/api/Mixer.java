package net.yakclient.mixin.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@code Mixer} marks a class as a Mixer. This is a alternate
 * for {@code IMixer} but should be used preferably.
 *
 * An example;
 *
 * <pre>
 * {@code
 * @Mixer(ExampleMixTo.class)
 *  public abstract class ExampleClass { ... }
 * }
 * </pre>
 *
 * @author Durgan McBroom
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Mixer {
    /**
     * {@code Mixer#type()} provides the class that will
     * be mixed to.
     *
     * @return returns the class that will be mixed into.
     */
    Class<?> value();
}
