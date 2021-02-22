package net.yakclient.mixin.api;

/**
 * Determines what type of injection should be performed
 * on a Mixin.
 *
 * @see Injection
 *
 * @author Durgan McBroom
 */
public enum InjectionType {
    AFTER_BEGIN,
    BEFORE_END,
    BEFORE_RETURN,
    BEFORE_INVOKE
}
