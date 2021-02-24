package net.yakclient.mixin.internal.bytecode;

import net.yakclient.mixin.internal.instruction.MethodInstructionForwarder;
import net.yakclient.mixin.internal.instruction.ProxyMethodForwarder;
import net.yakclient.mixin.registry.proxy.ProxiedPointer;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Set;

public class ByteCodeClassVisitor extends ClassVisitor {
//    private final Set<String> toFind;

    public ByteCodeClassVisitor(String... methods) {
        super(Opcodes.ASM6);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        final MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);
        if (visitor != null) return new MethodInstructionForwarder(visitor);
        return null;
    }

    public static class ByteCodeProxyVisitor extends ByteCodeClassVisitor {
        private final ProxiedPointer pointer;
        public ByteCodeProxyVisitor(ProxiedPointer pointer) {
            super();
            this.pointer = pointer;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            final MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);
            if (visitor != null) return new ProxyMethodForwarder(visitor, this.pointer);
            return null;
        }
    }
}
