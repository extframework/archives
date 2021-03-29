package net.yakclient.mixin.internal.methodadapter;

import org.objectweb.asm.MethodVisitor;

@FunctionalInterface
public interface ByteCodeConsumer {
    void accept(MethodVisitor visitor);
}
