package net.yakclient.mixin.api;

/**
 * Determines what type of injection should be performed
 * on a Mixin.
 *
 * @see Injection
 *
 * @author Durgan McBroom
 */
public interface InjectionType {
    /**
     * Will place the mixin at the very beginning of the method
     * before the first opcode.
     */
   int AFTER_BEGIN = 200;
    /**
     * Will place the mixin before the very last return statement
     * this will still apply in a void method.
     */
   int BEFORE_END = 201;
    /**
     * Will place the mixin before every single return in the method.
     */
   int BEFORE_RETURN = 202;
    /**
     * Will place the mixin before every single method invoke, Not ignoring
     * static calls.
     */
   int BEFORE_INVOKE = 203;
    /**
     * Will overwrite the method and replace it with the mixin, Note
     * if other mixins are targeted at this method they will still get placed accordingly.
     *
     * This is very dangerous and can cause issues with other mixins also targeting the same class. Additionally if
     */
   int OVERWRITE = 204;
}
