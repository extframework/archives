package net.yakclient.mixin.base.internal.bytecode

import net.yakclient.mixin.base.internal.bytecode.MixinSource.MixinProxySource
import net.yakclient.mixin.base.internal.instruction.DirectInsnInterceptor
import net.yakclient.mixin.base.internal.instruction.Instruction
import net.yakclient.mixin.base.internal.instruction.InstructionClassVisitor
import net.yakclient.mixin.base.internal.instruction.ProxyInsnInterceptor
import net.yakclient.mixin.base.internal.instruction.adapter.FieldSelfInsnAdapter
import net.yakclient.mixin.base.internal.instruction.adapter.InsnAdapter
import net.yakclient.mixin.base.internal.instruction.adapter.MethodSelfInsnAdapter
import net.yakclient.mixin.base.internal.instruction.adapter.ReturnRemoverInsnAdapter
import net.yakclient.mixin.base.internal.ASMType
import org.jetbrains.annotations.Contract
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.IOException
import java.util.*

class BytecodeMethodModifier {
    @Throws(IOException::class)
    fun <T : Instruction?> combine(classTo: String, vararg destinations: MixinDestination): ByteArray {
        val instructions = HashMap<String, Queue<QualifiedInstruction>>(destinations.size)
        require(destinations.size != 0) { "Must provide destinations to mixin!" }
        for (destination in destinations) {
            val current = PriorityQueue<QualifiedInstruction>()
            for (source in destination.sources) {
                val insn = applyInsnAdapters(
                    getInsn(source.location.cls, source),
                    classTo,
                    source.location.cls
                )
                current.add(
                    QualifiedInstruction(
                        source.location.priority,
                        source.location.injectionType,
                        insn
                    )
                )
            }
            instructions[destination.method] = current
        }
        return this.apply(instructions, classTo)
    }

    @Throws(IOException::class)
    private fun getInsn(cls: String, source: MixinSource): Instruction {
        val sourceReader = ClassReader(cls)
        val cv = InstructionClassVisitor(
            if (source is MixinProxySource) ProxyInsnInterceptor(source.pointer) else DirectInsnInterceptor(),
            source.location.method
        )
        sourceReader.accept(cv, 0)
        return cv.instructions
    }

    private fun applyInsnAdapters(instruction: Instruction, classTo: String, classFrom: String): Instruction {
        val returnAdapter: InsnAdapter = ReturnRemoverInsnAdapter()
        val fieldAdapter: InsnAdapter = FieldSelfInsnAdapter(returnAdapter, classTo, classFrom)
        val methodAdapter: InsnAdapter = MethodSelfInsnAdapter(fieldAdapter, classTo, classFrom)
        return methodAdapter.adapt(instruction)
    }

    @Contract(pure = true)
    @Throws(IOException::class)
    private fun apply(injectors: Map<String, Queue<QualifiedInstruction>>, mixin: String): ByteArray {
        val writer = ClassWriter(ClassWriter.COMPUTE_FRAMES)
        val adapter =
            if (ByteCodeUtils.DEFAULT_ASM_MODE == ASMType.TREE) TreeMixinCV(writer, injectors) else CoreMixinCV(
                writer,
                injectors
            )
        val reader = ClassReader(mixin)
        reader.accept(adapter, 0)
        return writer.toByteArray()
    }
}