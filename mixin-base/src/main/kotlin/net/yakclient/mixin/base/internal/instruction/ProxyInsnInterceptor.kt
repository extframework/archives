package net.yakclient.mixin.base.internal.instruction

import net.yakclient.mixin.base.registry.FunctionalProxy.ProxyResponseData
import net.yakclient.mixin.base.registry.proxy.MixinProxyManager
import org.objectweb.asm.Label
import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.*
import java.util.*

class ProxyInsnInterceptor(private val pointer: UUID) : DirectInsnInterceptor() {
    override fun intercept(): DirectInstruction {
        val insn = InsnList()
        val l0 = LabelNode(Label())
        val l1 = LabelNode(Label())
        val l2 = LabelNode(Label())
        val l3 = LabelNode(Label())

        val l4 = LabelNode(Label())
        insn.add(l0)
        insn.add(TypeInsnNode(Opcodes.NEW, "java/util/UUID"))
        insn.add(InsnNode(Opcodes.DUP))
        insn.add(LdcInsnNode(pointer.mostSignificantBits))
        insn.add(LdcInsnNode(pointer.leastSignificantBits))
        insn.add(MethodInsnNode(Opcodes.INVOKESPECIAL, "java/util/UUID", "<init>", "(JJ)V", false))
        val proxyResName = this.getRuntimeName(ProxyResponseData::class.java)

        insn.add(l1)
        insn.add(
            MethodInsnNode(
                Opcodes.INVOKESTATIC, getRuntimeName(
                    MixinProxyManager::class.java
                ), "proxy", "(Ljava/util/UUID;)L$proxyResName;", false
            )
        )
        insn.add(l2)
        insn.add(FieldInsnNode(Opcodes.GETFIELD, proxyResName, "cancel", "Z"))
        insn.add(JumpInsnNode(Opcodes.IFNE, l3))
        insn.add(l4)
        insn.add(super.intercept().insn)
        insn.add(l3)
        return DirectInstruction(insn)
    }

    private fun getRuntimeName(cls: Class<*>): String {
        return cls.name.replace("\\.".toRegex(), "/")
    }
}