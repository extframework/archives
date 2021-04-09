package net.yakclient.mixin.internal.instruction.core;

import org.objectweb.asm.MethodVisitor;

@FunctionalInterface
public interface ByteCodeConsumer {
    void accept(MethodVisitor visitor);
}
