package net.yakclient.mixin.api;

/**
 * This static interface is used to define default priorities.
 * It is recommended to use these but overloading with a
 * raw int would also work sufficiently.
 *
 * You can use priority as shown below;
 *
 * <pre>
 * {@code
 * @Injection(priority = Priority.HIGHEST)
 *  public void exampleMethod() { ... }
 * }
 * </pre>
 *
 * Additionally, overriding and using a raw int might look like
 * this;
 *
 *  <pre>
 *  * {@code
 *  * @Injection(priority = 15) //Arbitrary value
 *  *  public void exampleMethod() { ... }
 *  * }
 *  * </pre>
 *
 * @see Injection
 *
 * @author Durgan McBroom
 */
public interface Priority {
    int HIGHEST = 4;

    int HIGH = 3;

    int MEDIUM = 2;

    int LOW = 1;

    int LOWEST = 0;
}
