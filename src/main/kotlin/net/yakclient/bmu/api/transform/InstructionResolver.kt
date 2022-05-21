package net.yakclient.bmu.api.transform

import org.objectweb.asm.ClassReader
import org.objectweb.asm.tree.InsnList

/**
 * The basic structure for resolving/finding instructions.
 *
 * A basic example:
 * ```
 * val resolver : InstructionResolver = {
 *     ... resolving my instructions ...
 * }
 *
 * val resolveInsn = resolver.get()
 * ```
 *
 * @since 1.0-SNAPSHOT
 * @author Durgan McBroom
 */
public fun interface InstructionResolver {
    /**
     * Resolves the instructions, should need no arguments to
     * achieve this.
     *
     * @return The resolved instructions.
     */
    public fun get(): InsnList
}

/**
 * Reads instructions from the given source usually provided via constructor.
 */
public fun interface InstructionReader : InstructionResolver

/**
 * Returns the instructions provided via constructor.
 *
 * @constructor constructs with the given `InsnList`
 *
 * @author Durgan McBroom
 * @since 1.0-SNAPSHOT
 */
public class ProvidedInstructionReader(
    private val insn: InsnList
) : InstructionReader {
    override fun get(): InsnList = insn
}

/**
 * Reads the given instruction from the provided parent class
 * and the specified method provided.
 *
 * @constructor constructs given the parent class and method signature.
 *
 * @since 1.0-SNAPSHOT
 * @author Durgan McBroom
 */
public abstract class DirectInstructionReader(
    protected val reader: ClassReader,
    protected val methodSignature: String
) : InstructionReader

/**
 * The instruction resolver allows you to chain multiple
 * `InstructionResolver`s together and adapt instructions the
 * parents resolved instructions. The top level resolver should
 * almost always be an InstructionReader.
 *
 * An example:
 * ```
 * val reader = InstructionReader { ... }
 * val adapter1 = object: InstructionAdapter(reader) {
 *    override fun get(): InsnList = super.get().also { /* Transform */ }
 * }
 *
 * val insn = adapter1.get()
 * ```
 *
 * @constructor constructs with the given resolver as its parent.
 *
 * @since 1.0-SNAPSHOT
 * @author Durgan McBroom
 */
public abstract class InstructionAdapter(
    parent: InstructionResolver
) : InstructionResolver by parent

