package net.yakclient.archives.mixin

import net.yakclient.archives.extension.slice
import net.yakclient.archives.transform.ByteCodeUtils
import net.yakclient.archives.transform.InstructionResolver
import net.yakclient.archives.mixin.InjectionType
import org.objectweb.asm.tree.*

/**
 * Holds all Injection points that can be used in mixin bytecode
 * modification.
 *
 * @since 1.1-SNAPSHOT
 * @author Durgan McBroom
 */
internal object Injectors {
    /**
     * Finds the instruction directly before the last return statement.
     */
    private val BEFORE_END: MixinInjectionPoint = {
        val lastReturn = insn.fold<AbstractInsnNode, AbstractInsnNode?>(null) { acc, node ->
            if (ByteCodeUtils.isReturn(node.opcode)) node else acc
        } ?: throw IllegalStateException("Failed to find last return in given instructions.")

        listOf(BeforeInsnNodeInjector(insn, lastReturn))
    }

    /**
     * Finds the beginning of the provided instruction list.
     */
    private val AFTER_BEGIN: MixinInjectionPoint = { listOf(BeginningInjector(insn)) }

    /**
     * Finds all instructions before every single method invocation.
     */
    private val BEFORE_INVOKE: MixinInjectionPoint =
        { insn.filterIsInstance<MethodInsnNode>().map { BeforeInsnNodeInjector(insn, it) } }

    /**
     * Finds all instructions before every return statement.
     */
    private val BEFORE_RETURN: MixinInjectionPoint = {
        insn.filter { ByteCodeUtils.isReturn(it.opcode) }.map { BeforeInsnNodeInjector(insn, it) }
    }

    /**
     * Finds all instructions before the given opcode.
     */
    private val BEFORE_OPCODE: MixinInjectionPoint = { opcode ->
        insn.filter { it.opcode == opcode }.map { BeforeInsnNodeInjector(insn, it) }
    }

    /**
     * Provides an injector to overwrite all instructions and replace them
     * with the provided.
     */
    private val OVERWRITE: MixinInjectionPoint = {
        listOf(MixinInjector { toInject -> node.instructions = toInject.get() })
    }

    /**
     * Adapts given instruction and before injecting removes all returns
     * according to [RemoveLastReturn][InstructionAdapters.RemoveLastReturn]
     *
     * @constructor Creates an adapted injector that will inject into the given insn.
     */
    private abstract class MixinAdaptedInjector(
        protected val insn: InsnList
    ) : MixinInjector {
        /**
         * Injects after adding a resolver to remove the last return statement.
         *
         * @param toInject the instructions to adapt and inject.
         */
        override fun inject(toInject: InstructionResolver) =
            inject(FixLocals(RemoveLastReturn(toInject), sourceFollowingInjection()).get())

        /**
         * Abstractly injects the given insnList into the provided by construction.
         *
         * @param toInject the instructions to inject(already adapted).
         */
        abstract fun inject(toInject: InsnList)

        abstract fun sourceFollowingInjection() : InsnList
    }

    /**
     * Injects into the provided list before the given node.
     */
    private class BeforeInsnNodeInjector(
        insn: InsnList,
        private val node: AbstractInsnNode,
    ) : MixinAdaptedInjector(insn) {
        init {
            require(insn.contains(node)) { "Given InsnList does not contain the node provided to inject before." }
        }

        override fun inject(toInject: InsnList) = insn.insertBefore(node, toInject)
        override fun sourceFollowingInjection(): InsnList = insn.slice(node)
    }

    /**
     * Injects into the beginning of the given list.
     */
    private class BeginningInjector(
        insn: InsnList,
    ) : MixinAdaptedInjector(insn) {
        override fun inject(toInject: InsnList) = insn.insert(toInject)
        override fun sourceFollowingInjection(): InsnList = insn
    }

    /**
     * Finds the corresponding [injection point][MixinInjectionPoint] from the provided
     * int, correlating to the [injection type][net.yakclient.archives.api.InjectionType].
     * If the type does not match then an opcode matcher will be assumed.
     */
    fun of(id: InjectionType): MixinInjectionPoint = when (id) {
        InjectionType.AFTER_BEGIN -> AFTER_BEGIN
        InjectionType.BEFORE_END -> BEFORE_END
        InjectionType.BEFORE_RETURN -> BEFORE_RETURN
        InjectionType.BEFORE_INVOKE -> BEFORE_INVOKE
        InjectionType.OVERWRITE -> OVERWRITE
// TODO Figure out if we need this --->        else -> BEFORE_OPCODE
    }
}