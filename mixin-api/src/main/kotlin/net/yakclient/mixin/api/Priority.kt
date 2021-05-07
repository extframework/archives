package net.yakclient.mixin.api

/**
 * This static interface is used to define default priorities.
 * It is recommended to use these but overloading with a
 * raw int would also work sufficiently.
 *
 * You can use priority as shown below;
 *
 * <pre>
 * `= Priority.HIGHEST)
 * public void exampleMethod() { ... }
` *
</pre> *
 *
 * Additionally, overriding and using a raw int might look like
 * this;
 *
 * <pre>
 * * `* @Injection(priority = 15) //Arbitrary value
 * *  public void exampleMethod() { ... }
 * * `
 * * </pre>
 *
 * @see Injection
 *
 *
 * @author Durgan McBroom
 */
interface Priority {
    companion object {
        const val HIGHEST = 4
        const val HIGH = 3
        const val MEDIUM = 2
        const val LOW = 1
        const val LOWEST = 0
    }
}