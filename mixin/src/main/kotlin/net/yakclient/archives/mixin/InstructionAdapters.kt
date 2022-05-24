package net.yakclient.archives.mixin

import net.yakclient.archives.transform.ByteCodeUtils
import net.yakclient.archives.transform.InstructionAdapter
import net.yakclient.archives.transform.InstructionResolver
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.FieldInsnNode
import org.objectweb.asm.tree.InsnList
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.VarInsnNode

public class RemoveLastReturn(
    parent: InstructionResolver
) : InstructionAdapter(parent) {
    override fun get(): InsnList = super.get().also { insn ->
        val lastReturn: AbstractInsnNode? = insn.lastOrNull { it.opcode == Opcodes.RETURN }

        if (lastReturn != null) insn.remove(lastReturn)
    }
}

public class AlterThisReference(
    parent: InstructionResolver,
    private val clsTo: String,
    private val clsFrom: String
) : InstructionAdapter(parent) {
    override fun get(): InsnList = super.get().also {
        for (insn in it) {
            if (insn is MethodInsnNode && insn.opcode != Opcodes.INVOKESTATIC && insn.owner.equals(this.clsFrom)) insn.owner =
                this.clsTo

            if (insn is FieldInsnNode && insn.opcode != Opcodes.GETSTATIC && insn.owner.equals(this.clsFrom)) insn.owner =
                this.clsTo
        }
    }
}

// Fixes the locals between the given list and the adapted, make sure no collisions occur.
public class FixLocals(
    parent: InstructionResolver,
    // The insn that the resolved output of this class will be put before (the insn following the injection).
    private val insn: InsnList
) : InstructionAdapter(parent) {
    private data class LocalsMeta(
        // Locals that are required in the given instruction set before execution.
        val requiredLocals: Set<Int>,
        // Locals that are set before use in the given instruction set and are not needed to be any value beforehand.
        val overridableLocals: Set<Int>,
        // The largest local in the instruction set
        val largestLocal: Int
    )

    private fun localsMetaFor(list: InsnList): LocalsMeta {
        val stores = HashSet<Int>()
        val requiredLocals = HashSet<Int>()
        val overridableLocals = HashSet<Int>()
        var largestLocal = 0

        // Could make this a fold and then also get the largest local at the same time, but would be alot messier and no faster.
        list.forEach {
            if (it is VarInsnNode) {
                val o = it.opcode
                val v = it.`var`

                if (ByteCodeUtils.isStore(o)) stores.add(v)
                else if (ByteCodeUtils.isLoad(o)) {
                    if (stores.contains(v)) overridableLocals.add(v)
                    else requiredLocals.add(v)

                    if (v > largestLocal) largestLocal = v
                }
            }
        }

        return LocalsMeta(requiredLocals, overridableLocals, largestLocal)
    }

    override fun get(): InsnList {
        // Get the instructions to inject
        val injectedInsn = super.get()

        // Get the meta for insn(the insn following the injection)
        val (requiredLocals, ol, largestLocal) = localsMetaFor(insn)
        val overridableLocals =
            ol.toList() // Convert the overrides to a list, so we can access them by index(order doesn't matter, but we want an index to always point to the same local)

        // Get meta for the injected instructions, only need the required locals(method parameters and self ref)
        val (requiredInjectionLocals, _, _) = localsMetaFor(injectedInsn)

        // Create a map of the locals that we need to replace to not interfere with the following insn list.
        val replaceMap: Map<Int, Int> = requiredLocals
            .subtract(requiredInjectionLocals) // We dont care about instructions that both list require(self ref and parameters again)
            .withIndex() // Attach index for use when pairing with new locals
            .associateWith { (i, _) ->
                overridableLocals.getOrNull(i) // First check if we can use a local that is safe to override(saves slight amounts of memory)
                    ?: (largestLocal + (i + 1)) // If we have no more overridable locals we can "create a new one".
            }.mapKeys { it.key.value } // Make the keys to the old local value

        // Iterate through and if it's an instruction referencing a local, change it.
        injectedInsn.forEach {
            if (it is VarInsnNode) {
                if (ByteCodeUtils.isStore(it.opcode) || ByteCodeUtils.isLoad(it.opcode))
                    it.`var` = replaceMap[it.`var`] ?: it.`var`
            }
        }

        // Return the now modified instruction list.
        return injectedInsn
    }
}
