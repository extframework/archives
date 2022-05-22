package net.yakclient.archives.transform

/**
 * Represents a method signature and its 3 parts; A name, a description(parameters)
 * and a (optional) return type.
 *
 * @constructor Constructs a MethodSignature with the given name, description, and return type.
 *
 * @since 1.1-SNAPSHOT
 * @author Durgan McBroom
 */
public data class MethodSignature(
    val name: String,
    val desc: String,
    val returnType: String?
) {
    /**
     * Checks if the name and description of the other Signature match this one.
     *
     * @param other The signature to match against.
     * @return if they match.
     */
    public fun matches(other: MethodSignature): Boolean =
        (other.name == name && other.desc == desc) && (if (returnType == null || other.returnType == null) true else returnType == other.returnType)

    public companion object {
        private const val NON_ARRAY_PATTERN = "[ZCBSIFJD]|(?:L.+;)"

        private const val ANY_VALUE_PATTERN = "$NON_ARRAY_PATTERN|(?:\\[+(?:$NON_ARRAY_PATTERN))"

        private const val SIGNATURE_PATTERN = "^(.*)\\(((?:$ANY_VALUE_PATTERN)*)\\)($ANY_VALUE_PATTERN|V)?$"

        /**
         * Parses a method signature into a MethodSignature.
         *
         * @param signature the unparsed signature to parse.
         * @return the signature if valid.
         *
         * @throws IllegalStateException If the given signature is invalid.
         */
        public fun of(signature: String): MethodSignature {
            val regex = Regex(SIGNATURE_PATTERN)
            check(regex.matches(signature)) { "Invalid method signature: $signature" }
            return regex.find(signature)!!.groupValues.let { values ->
                MethodSignature(
                    values[1],
                    values[2],
                    values[3].takeIf { it.isNotBlank() }
                )
            }
        }
    }
}