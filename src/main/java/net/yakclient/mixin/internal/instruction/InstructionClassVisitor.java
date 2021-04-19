package net.yakclient.mixin.internal.instruction;

import net.yakclient.YakMixins;
import net.yakclient.mixin.internal.bytecode.ByteCodeUtils;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class InstructionClassVisitor extends ClassVisitor {
    private final String targetMethod;
    private final InstructionInterceptor interceptor;

    public InstructionClassVisitor(InstructionInterceptor interceptor, String targetMethod) {
        super(YakMixins.ASM_VERSION, new ClassNode());
        this.interceptor = interceptor;
        this.targetMethod = targetMethod;
    }

    public Instruction getInstructions() {
        return this.interceptor.intercept();
    }


    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        final MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);
        if (visitor != null && targetMethod != null && ByteCodeUtils.descriptorsSame(targetMethod, name + desc) ) {
            if (!(this.interceptor instanceof MethodVisitor)) throw new IllegalArgumentException("InstructionInterceptor must inherit from the MethodVisitor");
            return (MethodVisitor) this.interceptor;
        }
        return visitor;
    }
}
