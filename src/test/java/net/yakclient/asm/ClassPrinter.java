package net.yakclient.asm;

import net.yakclient.mixin.api.Injection;
import net.yakclient.mixin.api.Priority;
import org.objectweb.asm.*;

public class ClassPrinter extends ClassVisitor {
    public ClassPrinter() {
        super(Opcodes.ASM9);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc,
                                   String signature, Object value) {
        System.out.println(" " + desc + " " + name);
        return null;
    }
    @Override
    public MethodVisitor visitMethod(int access, String name,
                                     String desc, String signature, String[] exceptions) {
        System.out.println(" " + name + desc);
        return null;
    }
    @Override
    public void visitEnd() {
        System.out.println("}");
    }
}
