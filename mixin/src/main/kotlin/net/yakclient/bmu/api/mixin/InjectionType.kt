package net.yakclient.bmu.api.mixin

/**
 * Determines what type of injection should be performed
 * on a Mixin.
 *
 * @see Injection
 *
 *
 * @author Durgan McBroom
 */
public enum class InjectionType {
    /**
     * Will place the mixin at the very beginning of the method
     * before the first opcode.
     */
    AFTER_BEGIN,

    /**
     * Will place the mixin before the very last return statement
     * this will still apply in a void method.
     */
    BEFORE_END,

    /**
     * Will place the mixin before every single return in the method.
     */
    BEFORE_RETURN,

    /**
     * Will place the mixin before every single method invoke, Not ignoring
     * static calls.
     */
    BEFORE_INVOKE,

    /**
     * Will overwrite the method and replace it with the mixin, Note
     * if other mixins are targeted at this method they will still get placed accordingly.
     *
     * This is very dangerous and can cause issues with other mixins also targeting the same class. Additionally if
     */
    OVERWRITE;
}