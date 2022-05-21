package net.yakclient.bmu.api.transform

import net.yakclient.bmu.api.ArchiveHandle
import net.yakclient.common.util.runCatching
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.ClassNode

public class AwareClassWriter(
    private val handle: ArchiveHandle,
    flags: Int,
    reader: ClassReader? = null,
) : ClassWriter(reader, flags) {
    public override fun getCommonSuperClass(type1: String, type2: String): String {
        // type1 and type2 come in the jvm internal class name format

        val tree1 = loadType(type1)
        val tree2 = loadType(type2)

        // If one is a child of another, we can just return the parent
        if (tree1.isChildOf(tree2.name)) return tree2.name
        if (tree2.isChildOf(tree1.name)) return tree1.name

        // If either is an interface, and they are not children of each other, the common parent must be Object.
        if (tree1.isInterface || tree2.isInterface) return "java/lang/Object"

        var superType = tree1

        do {
            superType = (superType.superNode ?: return "java/lang/Object")
        } while (!tree2.isChildOf(superType.name))

        return superType.name
    }

    private interface HierarchyNode {
        val superNode: HierarchyNode?
        val interfaceNodes: List<HierarchyNode>

        val name: String
        val isInterface: Boolean

        fun isChildOf(name: String): Boolean =
            ((superNode?.let { listOf(it) } ?: listOf()) + interfaceNodes).any {
                it.name == name || it.isChildOf(
                    name
                )
            }
    }

    private inner class LoadedClassNode(
        type: Class<*>,
    ) : HierarchyNode {
        override val superNode: HierarchyNode? = type.superclass?.let(::LoadedClassNode)
        override val interfaceNodes: List<HierarchyNode> = type.interfaces.map(::LoadedClassNode)
        override val name: String = type.name.replace('.', '/')
        override val isInterface: Boolean = type.isInterface
    }

    private inner class UnloadedClassNode(
        node: ClassNode,
    ) : HierarchyNode {
        override val superNode: HierarchyNode? = node.superName?.let(::loadType)
        override val interfaceNodes: List<HierarchyNode> = node.interfaces.map(::loadType)
        override val name = node.name!!
        override val isInterface: Boolean = node.access and Opcodes.ACC_INTERFACE != 0
    }

    // Name expected in JVM internal format
    private fun loadType(name: String): HierarchyNode {
        val openEntry = handle.reader["$name.class"]?.resource?.open()
            ?: return runCatching(ClassNotFoundException::class) {
                LoadedClassNode(Class.forName(name.replace('/', '.')))
            } ?: throw TypeNotPresentException(name, null)

        val node = ClassNode()


        ClassReader(openEntry).accept(node, 0)
        return UnloadedClassNode(node)
    }
}