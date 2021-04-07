package net.yakclient.asm.method;

import net.yakclient.mixin.internal.bytecode.ByteCodeUtils;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ASMClassAdapter extends ClassVisitor {
    public ASMClassAdapter( ClassVisitor cv) {
        super(ByteCodeUtils.ASM_VERSION, cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        final MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if (!name.equals("<init>")) return new ASMMethodAdapter(mv);
        return mv;
    }
}
