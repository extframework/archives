package net.yakclient.mixin.api;

/**
 * {@code IMixer} specifies a class to be a Mixer. To use
 * it effectively {@code  Class<?> type()} MUST be
 * overridden. This class is a substitute for @Mixer.
 *
 * An example;
 *
 * <pre>
 * {@code
 *  public abstract class ExampleClass implements IMixer {
 *      public Class<?> type() {
 *          return ExampleMixTo.class;
 *      }
 *  }
 * }
 * </pre>
 *
 * @see Mixer
 *
 * @author Durgan McBroom
 */
public interface IMixer {
    /**
     * {@code IMixer#type()} provides the class that will
     * be mixed to.
     *
     * @return returns the class that will be mixed into.
     */
    String type();
}
