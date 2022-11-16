package net.yakclient.archives.mixin

import net.yakclient.archives.extension.slice
import net.yakclient.archives.transform.ByteCodeUtils
import net.yakclient.archives.transform.InstructionResolver
import net.yakclient.archives.transform.MethodTransformer
import net.yakclient.archives.transform.TransformerConfig
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode

public object SourceInjection : MixinInjection<SourceInjectionData> {
    override fun apply(
        data: SourceInjectionData
    ): TransformerConfig.MutableTransformerConfiguration = TransformerConfig.of {
        val source = AlterThisReference(
            data.insnResolver,
            data.classTo.replace('.', '/'),
            data.classSelf.replace('.', '/')
        )

        val point = data.methodAt
        val signature = data.methodTo

        transformMethod(
            signature,
            SourceInjectionTransformer(point, source)
        )
    }
}

public data class SourceInjectionData(
    val classTo: String,
    val classSelf: String,

    public val insnResolver: InstructionResolver,
    public val methodTo: String,
    public val methodAt: SourceInjectionPoint,
) : MixinInjection.InjectionData


internal class SourceInjectionTransformer(
    private val point: SourceInjectionPoint,
    private val source: InstructionResolver
) : MethodTransformer {
    override fun invoke(context: MethodNode): MethodNode = context.apply {
        point.apply(context).forEach { it.inject(source) }
    }
}

public data class SourceInjectionContext(
    val node: MethodNode
) {
    val insn: InsnList = node.instructions
}

private fun SourceInjectionPoint.apply(node: MethodNode): List<MethodSourceInjector> =
    apply(SourceInjectionContext(node))

public fun interface MethodSourceInjector {
    public fun inject(toInject: InstructionResolver)
}

public fun interface SourceInjectionPoint {
    public fun apply(context: SourceInjectionContext): List<MethodSourceInjector>
}

/**
 * Holds all Injection points that can be used in mixin bytecode
 * modification.
 *
 * @since 1.1-SNAPSHOT
 * @author Durgan McBroom
 */
public object SourceInjectors {
    /**
     * Finds the instruction directly before the last return statement.
     */
    public val BEFORE_END: SourceInjectionPoint = SourceInjectionPoint { context ->
        val lastReturn = context.insn.fold<AbstractInsnNode, AbstractInsnNode?>(null) { acc, node ->
            if (ByteCodeUtils.isReturn(node.opcode)) node else acc
        } ?: throw IllegalStateException("Failed to find last return in given instructions.")

        listOf(BeforeInsnNodeInjector(context.insn, lastReturn))
    }

    /**
     * Finds the beginning of the provided instruction list.
     */
    public val AFTER_BEGIN: SourceInjectionPoint = SourceInjectionPoint { listOf(BeginningInjector(it.insn)) }

    /**
     * Finds all instructions before every single method invocation.
     */
    public val BEFORE_INVOKE: SourceInjectionPoint =
        SourceInjectionPoint { context ->
            context.insn.filterIsInstance<MethodInsnNode>().map { BeforeInsnNodeInjector(context.insn, it) }
        }

    /**
     * Finds all instructions before every return statement.
     */
    public val BEFORE_RETURN: SourceInjectionPoint = SourceInjectionPoint { context ->
        context.insn.filter { ByteCodeUtils.isReturn(it.opcode) }.map { BeforeInsnNodeInjector(context.insn, it) }
    }

    /**
     * Finds all instructions before the given opcode.
     */
    public fun BEFORE_OPCODE(opcode: Int): SourceInjectionPoint = SourceInjectionPoint { context ->
        context.insn.filter { it.opcode == opcode }.map { BeforeInsnNodeInjector(context.insn, it) }
    }

    /**
     * Provides an injector to overwrite all instructions and replace them
     * with the provided.
     */
    public val OVERWRITE: SourceInjectionPoint = SourceInjectionPoint { context ->
        listOf(MethodSourceInjector { toInject -> context.node.instructions = toInject.get() })
    }
}

/**
 * Adapts given instruction and before injecting removes all returns
 * according to [RemoveLastReturn][InstructionAdapters.RemoveLastReturn]
 *
 * @constructor Creates an adapted injector that will inject into the given insn.
 */
public abstract class MixinAdaptedInjector(
    protected val insn: InsnList
) : MethodSourceInjector {
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