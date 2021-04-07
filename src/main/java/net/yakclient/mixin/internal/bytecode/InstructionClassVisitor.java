package net.yakclient.mixin.internal.bytecode;

import net.yakclient.mixin.internal.instruction.Instruction;
import net.yakclient.mixin.internal.instruction.MethodInstructionForwarder;
import net.yakclient.mixin.internal.instruction.ProxyMethodForwarder;
import org.objectweb.asm.*;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InstructionClassVisitor extends ClassVisitor {
    protected final String targetMethod;
    protected boolean found = false;
    protected MethodInstructionForwarder forwarder;
    protected final String source;
    protected final String dest;

    private static final String VOID_RETURN_PATTERN = "[(].*[)]V";

    public InstructionClassVisitor(ClassVisitor visitor, String targetMethod, String source, String dest) {
        super(ByteCodeUtils.ASM_VERSION, visitor);
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

    protected final MethodVisitor visitSuperMethod(int access, String name, String desc, String signature, String[] exceptions) {
        return super.visitMethod(access, name, desc, signature, exceptions);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        final MethodVisitor visitor = this.visitSuperMethod(access, name, desc, signature, exceptions);
        if (visitor != null && targetMethod != null && ByteCodeUtils.descriptorsSame(targetMethod, name + desc) && !found) {
            this.found = true;
            return this.forwarder = new MethodInstructionForwarder(visitor, this.source, this.dest);
        }
        return visitor;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        return super.visitField(access, name, descriptor, signature, value);
    }

    boolean shouldReturn(String signature) {
        Pattern r = Pattern.compile(VOID_RETURN_PATTERN);
        Matcher m = r.matcher(signature);
        return !m.find();
    }

    public static class InstructionProxyVisitor extends InstructionClassVisitor {
        private final UUID pointer;

        public InstructionProxyVisitor(ClassVisitor visitor, String targetMethod, String source, String dest, UUID pointer) {
            super(visitor, targetMethod, source, dest);
            this.pointer = pointer;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            final MethodVisitor visitor = this.visitSuperMethod(access, name, desc, signature, exceptions);
            if (visitor != null && targetMethod != null && ByteCodeUtils.descriptorsSame(targetMethod, name + desc) && !found) {
                this.found = true;
                return this.forwarder = new ProxyMethodForwarder(visitor, this.shouldReturn(desc), this.source, this.dest, this.pointer);
            }
            return visitor;
        }
    }
}
