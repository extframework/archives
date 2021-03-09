package net.yakclient.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class RemoveMethodAdapter extends ClassVisitor {
    public RemoveMethodAdapter(ClassWriter writer) {
        super(Opcodes.ASM9, writer);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        if ("<init>".equals(name) || "create".equals(name)) return super.visitMethod(access, name, desc, signature, exceptions);
        return null;
    }


}
