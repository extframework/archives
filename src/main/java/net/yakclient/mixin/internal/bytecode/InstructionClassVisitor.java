package net.yakclient.mixin.internal.bytecode;

import net.yakclient.mixin.internal.instruction.Instruction;
import net.yakclient.mixin.internal.instruction.MethodInstructionForwarder;
import net.yakclient.mixin.internal.instruction.ProxyMethodForwarder;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InstructionClassVisitor extends ClassVisitor {
    //    private final Set<String> toFind;
    private final String targetMethod;
    private boolean found = false;
    private MethodInstructionForwarder forwarder;
    protected final Class<?> source;
    protected final Class<?> dest;

    private static final String VOID_RETURN_PATTERN = "(.*)V";

    public InstructionClassVisitor(ClassVisitor visitor, String targetMethod, Class<?> source, Class<?> dest) {
        super(Opcodes.ASM6, visitor);
        this.targetMethod = targetMethod;
        this.source = source;
        this.dest = dest;
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
            return this.forwarder = new MethodInstructionForwarder(visitor, this.shouldReturn(desc), this.source.getName(), this.dest.getName());
        }
        return visitor;
    }

    boolean shouldReturn(String signature) {
        Pattern r = Pattern.compile(VOID_RETURN_PATTERN);
        Matcher m = r.matcher(signature);
        return !m.find();
    }

    public static class InstructionProxyVisitor extends InstructionClassVisitor {
        private final UUID pointer;

        public InstructionProxyVisitor(ClassVisitor visitor, String targetMethod, Class<?> source, Class<?> dest, UUID pointer) {
            super(visitor, targetMethod, source, dest);
            this.pointer = pointer;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            final MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);
            if (visitor != null)
                return new ProxyMethodForwarder(visitor, this.shouldReturn(signature), this.source.getName(), this.dest.getName(), this.pointer);
            return null;
        }
    }
}
