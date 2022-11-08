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
public object Injectors {
    /**
     * Finds the instruction directly before the last return statement.
     */
    public val BEFORE_END: MixinInjectionPoint = MixinInjectionPoint { context ->
        val lastReturn = context.insn.fold<AbstractInsnNode, AbstractInsnNode?>(null) { acc, node ->
            if (ByteCodeUtils.isReturn(node.opcode)) node else acc
        } ?: throw IllegalStateException("Failed to find last return in given instructions.")

        listOf(BeforeInsnNodeInjector(context.insn, lastReturn))
    }

    /**
     * Finds the beginning of the provided instruction list.
     */
    public val AFTER_BEGIN: MixinInjectionPoint = MixinInjectionPoint { listOf(BeginningInjector(it.insn)) }

    /**
     * Finds all instructions before every single method invocation.
     */
    public val BEFORE_INVOKE: MixinInjectionPoint =
        MixinInjectionPoint { context ->
            context.insn.filterIsInstance<MethodInsnNode>().map { BeforeInsnNodeInjector(context.insn, it) }
        }

    /**
     * Finds all instructions before every return statement.
     */
    public val BEFORE_RETURN: MixinInjectionPoint = MixinInjectionPoint { context ->
        context.insn.filter { ByteCodeUtils.isReturn(it.opcode) }.map { BeforeInsnNodeInjector(context.insn, it) }
    }

    /**
     * Finds all instructions before the given opcode.
     */
    public fun BEFORE_OPCODE(opcode: Int): MixinInjectionPoint = MixinInjectionPoint { context ->
        context.insn.filter { it.opcode == opcode }.map { BeforeInsnNodeInjector(context.insn, it) }
    }

    /**
     * Provides an injector to overwrite all instructions and replace them
     * with the provided.
     */
    public val OVERWRITE: MixinInjectionPoint = MixinInjectionPoint { context ->
        listOf(MixinInjector { toInject -> context.node.instructions = toInject.get() })
    }

    /**
     * Adapts given instruction and before injecting removes all returns
     * according to [RemoveLastReturn][InstructionAdapters.RemoveLastReturn]
     *
     * @constructor Creates an adapted injector that will inject into the given insn.
     */
    public abstract class MixinAdaptedInjector(
        protected val insn: InsnList
    ) : MixinInjector {
        /**
         * Injects after adding a resolver to remove the last return statement.
         *
         * @param toInject the instructions to adapt and inject.
         */
        override fun inject(toInject: InstructionResolver): Unit =
            inject(FixLocals(RemoveLastReturn(toInject), sourceFollowingInjection()).get())

        /**
         * Abstractly injects the given insnList into the provided by construction.
         *
         * @param toInject the instructions to inject(already adapted).
         */
        public abstract fun inject(toInject: InsnList)

        public abstract fun sourceFollowingInjection(): InsnList
    }

    /**
     * Injects into the provided list before the given node.
     */
    public class BeforeInsnNodeInjector(
        insn: InsnList,
        private val node: AbstractInsnNode,
    ) : MixinAdaptedInjector(insn) {
        init {
            require(insn.contains(node)) { "Given InsnList does not contain the node provided to inject before." }
        }

        override fun inject(toInject: InsnList): Unit = insn.insertBefore(node, toInject)
        override fun sourceFollowingInjection(): InsnList = insn.slice(node)
    }

    /**
     * Injects into the beginning of the given list.
     */
    public class BeginningInjector(
        insn: InsnList,
    ) : MixinAdaptedInjector(insn) {
        override fun inject(toInject: InsnList): Unit = insn.insert(toInject)
        override fun sourceFollowingInjection(): InsnList = insn
    }

    /**
     * Finds the corresponding [injection point][MixinInjectionPoint] from the provided
     * int, correlating to the [injection type][net.yakclient.archives.api.InjectionType].
     * If the type does not match then an opcode matcher will be assumed.
     */
//    fun of(id: InjectionType): MixinInjectionPoint = when (id) {
//        InjectionType.AFTER_BEGIN -> AFTER_BEGIN
//        InjectionType.BEFORE_END -> BEFORE_END
//        InjectionType.BEFORE_RETURN -> BEFORE_RETURN
//        InjectionType.BEFORE_INVOKE -> BEFORE_INVOKE
//        InjectionType.OVERWRITE -> OVERWRITE
//// TODO Figure out if we need this --->        else -> BEFORE_OPCODE
//    }
}