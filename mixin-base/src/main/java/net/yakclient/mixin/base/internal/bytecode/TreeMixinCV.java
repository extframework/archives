package net.yakclient.mixin.base.internal.bytecode;

import net.yakclient.mixin.base.internal.methodadapter.MixinPatternMatcher;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.ClassNode;

import java.util.Map;
import java.util.Queue;

public class TreeMixinCV extends MixinCV {
    private final ClassVisitor parent;

    public TreeMixinCV(ClassVisitor parent, Map<String, Queue<QualifiedInstruction>> injectors) {
        super(new ClassNode(), injectors);
        this.parent = parent;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        final MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);

        final var qualifiedName = name + desc;
        if (this.hasInjection(qualifiedName)) {
            final Queue<QualifiedInstruction> instructions = this.getInjection(qualifiedName);
            final MixinPatternMatcher.TreeMixinPatternMatcherBuilder builder = MixinPatternMatcher.treePatternMatcher();
            for (var insn : instructions) {
                builder.addNode(insn.getInjectionType(), insn.getInsn(), insn.getPriority());
            }
            return builder.build(visitor);
        }

        return visitor;
    }

    @Override
    public void visitEnd() {
        ((ClassNode) this.cv).accept(this.parent);
    }
}
