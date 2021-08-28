package net.yakclient.mixins.base

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode
import java.util.concurrent.CompletableFuture

class InjectionConfig(
    val isAsync: Boolean = false,
    private val resolver: ClassResolver
) {
    fun apply(name: String) : CompletableFuture<Class<*>> = runBlocking {
        val future = CompletableFuture<Class<*>>()
        val job = launch {
            future.complete(applySuspendable(name))
        }

        if (!isAsync) job.join()
        future
    }

    private fun applySuspendable(name: String): Class<*> {
        TODO("")
//        val classReader = ClassReader(name)
//
//        val writer = ClassWriter(ClassWriter.COMPUTE_FRAMES)
//
//        classReader.accept(ClassNodeAccepter(writer, ClassNode()), 0)
//
//        val def = ClassDefinition(Class.forName(name), writer.toByteArray())
//
//        YakMixinsAgent.instrumentation.redefineClasses(def)
    }

    class ClassNodeAccepter(
        private val parent: ClassVisitor,
        node: ClassNode
    ) : ClassVisitor(Opcodes.ASM9, node) {
        override fun visitEnd() {
            (cv as ClassNode).accept(parent)
            super.visitEnd()
        }
    }
}