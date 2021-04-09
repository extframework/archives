package net.yakclient.mixin.internal.instruction;

import net.yakclient.YakMixins;
import net.yakclient.mixin.internal.bytecode.ByteCodeUtils;
import net.yakclient.mixin.internal.instruction.InstructionInterceptor;
import net.yakclient.mixin.internal.instruction.core.CoreInsnInterceptor;
import net.yakclient.mixin.internal.instruction.tree.ProxyTreeInsnInterceptor;
import net.yakclient.mixin.internal.instruction.tree.TreeInsnInterceptor;
import org.objectweb.asm.*;

public class InstructionClassVisitor extends ClassVisitor {
    private final String targetMethod;
    private final InstructionInterceptor<?> interceptor;
    private final String source;
    private final String dest;

//    private static final String VOID_RETURN_PATTERN = "[(].*[)]V";

    public InstructionClassVisitor(InstructionInterceptor<?> interceptor, String targetMethod, String source, String dest) {
        super(YakMixins.ASM_VERSION);
        this.interceptor = interceptor;
        this.targetMethod = targetMethod;
        this.source = source;
        this.dest = dest;
    }

    public Instruction getInstructions() {
        return this.interceptor.intercept();
    }


//    public CoreInstruction getInsn() {
//        return this.forwarder.toInstructions();
//    }

//    protected final MethodVisitor visitSuperMethod(int access, String name, String desc, String signature, String[] exceptions) {
//        return super.visitMethod(access, name, desc, signature, exceptions);
//    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        final MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);
        if (visitor != null && targetMethod != null && ByteCodeUtils.descriptorsSame(targetMethod, name + desc) ) {
            return (MethodVisitor) this.interceptor;
        }
        return visitor;
    }

//    @Override
//    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
//        return super.visitField(access, name, descriptor, signature, value);
//    }

//    public static class InsnClsVisitorBuilder {
//        private final String targetMethod;
//        private final InstructionInterceptor<?> interceptor;
//        private final String source;
//        private final String dest;
//
//        public InsnClsVisitorBuilder(InterceptorType type, String targetMethod, String source, String dest) {
//            switch (type) {
//                case CORE:
//                    this.interceptor = new CoreInsnInterceptor(source, dest);
//                    break;
//                case TREE:
//                    this.interceptor = new TreeInsnInterceptor();
//                    break;
//                case CORE_PROXY:
//                    this.interceptor - new ProxyTreeInsnInterceptor()
//                    break;
//                case TREE_PROXY:
//                    break;
//            }
//            this.targetMethod = targetMethod;
//            this.source = source;
//            this.dest = dest;
//        }
//    }

//    public enum InterceptorType {
//        CORE,
//        TREE,
//        CORE_PROXY,
//        TREE_PROXY;
//    }

//    public enum ASMType {
//        CORE,
//        TREE;
//    }
//
//    public enum INJECTION_WRAPPER {
//        PROXY,
//        NONE;
//    }

//    public static final Supplier<TreeInsnInterceptor> TREE_INSTRUCTION;
//    public static final Supplier<CoreInsnInterceptor> CORE_INSTRUCTION = CoreInsnInterceptor::new;
//    public enum InterceptorType {
//
//    }
//    boolean shouldReturn(String signature) {
//        Pattern r = Pattern.compile(VOID_RETURN_PATTERN);
//        Matcher m = r.matcher(signature);
//        return !m.find();
//    }

//    public static class InstructionProxyVisitor extends InstructionClassVisitor {
//        private final UUID pointer;
//
//        public InstructionProxyVisitor(ClassVisitor visitor, String targetMethod, String source, String dest, UUID pointer) {
//            super(visitor, targetMethod, source, dest);
//            this.pointer = pointer;
//        }
//
//        @Override
//        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
//            final MethodVisitor visitor = this.visitSuperMethod(access, name, desc, signature, exceptions);
//            if (visitor != null && targetMethod != null && ByteCodeUtils.descriptorsSame(targetMethod, name + desc) && !found) {
//                this.found = true;
//                return this.forwarder = new ProxyCoreInsnInterceptor(visitor, this.source, this.dest, this.pointer);
//            }
//            return visitor;
//        }
//    }
}
