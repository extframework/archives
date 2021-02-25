package net.yakclient.mixin.internal.bytecode;

import net.yakclient.mixin.internal.instruction.Instruction;
import net.yakclient.mixin.internal.instruction.MethodInstructionForwarder;
import net.yakclient.mixin.internal.instruction.ProxyMethodForwarder;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.UUID;

public class InstructionClassVisitor extends ClassVisitor {
    //    private final Set<String> toFind;
    private final String targetMethod;
    private boolean found = false;
    private MethodInstructionForwarder forwarder;

    public InstructionClassVisitor(ClassVisitor visitor, String targetMethod) {
    super(Opcodes.ASM6,visitor);
        this.targetMethod = targetMethod;
    }

    public boolean found() {
        return this.found;
    }

    public Instruction getInsn() {
        return this.forwarder.toInstructions();
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        final MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);
        if (visitor != null && targetMethod != null && targetMethod.equals(name) && !found) {
            this.found = true;
            return this.forwarder = new MethodInstructionForwarder(visitor);
        }
        return visitor;
    }

    public static class InstructionProxyVisitor extends InstructionClassVisitor {
        private final UUID pointer;

        public InstructionProxyVisitor(ClassWriter writer, UUID pointer, String targetMethod) {
            super(writer, targetMethod);
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
