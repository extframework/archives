package net.yakclient.bmu.api.mixin.annotations

/**
 * Determines what type of injection should be performed
 * on a Mixin.
 *
 * @see Injection
 *
 *
 * @author Durgan McBroom
 */
public interface InjectionType {
    public companion object {
        /**
         * Will place the mixin at the very beginning of the method
         * before the first opcode.
         */
        public const val AFTER_BEGIN : Int = 200

        /**
         * Will place the mixin before the very last return statement
         * this will still apply in a void method.
         */
        public const val BEFORE_END : Int = 201

        /**
         * Will place the mixin before every single return in the method.
         */
        public const val BEFORE_RETURN : Int = 202

        /**
         * Will place the mixin before every single method invoke, Not ignoring
         * static calls.
         */
        public const val BEFORE_INVOKE : Int = 203

        /**
         * Will overwrite the method and replace it with the mixin, Note
         * if other mixins are targeted at this method they will still get placed accordingly.
         *
         * This is very dangerous and can cause issues with other mixins also targeting the same class. Additionally if
         */
        public const val OVERWRITE : Int = 204
    }
}