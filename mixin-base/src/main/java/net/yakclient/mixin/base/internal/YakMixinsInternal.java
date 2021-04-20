package net.yakclient.mixin.base.internal;

import net.yakclient.mixin.api.InjectionType;
import net.yakclient.mixin.base.internal.instruction.Instruction;
import net.yakclient.mixin.base.internal.methodadapter.core.*;
import net.yakclient.mixin.base.internal.methodadapter.tree.*;
import org.objectweb.asm.MethodVisitor;

public class YakMixinsInternal {
    public static CoreMixinPatternMatcher corePatternMatcher(int type, MethodVisitor parent, Instruction insn) {
        switch (type) {
            case InjectionType.AFTER_BEGIN:
                return new CoreABPatternMatcher(parent, insn);
            case InjectionType.BEFORE_INVOKE:
                return new CoreBIPatternMatcher(parent, insn);
            case InjectionType.BEFORE_RETURN:
                return new CoreBRPatternMatcher(parent, insn);
            case InjectionType.BEFORE_END:
                return new CoreBEPatternMatcher(parent, insn);
            case InjectionType.OVERWRITE:
                return new CoreOverwritePatternMatcher(parent, insn);
            default:
                return new CoreOpcodePatternMatcher(parent, insn, type);
        }
    }

    public static TreeMixinPatternMatcherBuilder treePatternMatcher() {
        return new TreeMixinPatternMatcherBuilder();
    }

    public static final class TreeMixinPatternMatcherBuilder {
        private final ASMMethodProxy.MethodProxyBuilder builder;

        private TreeMixinPatternMatcherBuilder() {
            this.builder = new ASMMethodProxy.MethodProxyBuilder();
        }

        public TreeMixinPatternMatcherBuilder addNode(int type, Instruction insn, int priority) {
            switch (type) {
                case InjectionType.AFTER_BEGIN:
                    this.builder.addMatcher(new TreeABPatternMatcher(insn), priority);
                case InjectionType.BEFORE_INVOKE:
                    this.builder.addMatcher(new TreeBIPatternMatcher(insn), priority);
                case InjectionType.BEFORE_RETURN:
                    this.builder.addMatcher(new TreeBRPatternMatcher(insn), priority);
                case InjectionType.BEFORE_END:
                    this.builder.addMatcher(new TreeBEPatternMatcher(insn), priority);
                case InjectionType.OVERWRITE:
                    this.builder.addMatcher(new TreeOverwritePatternMatcher(insn), priority);
                default:
                    this.builder.addMatcher(new TreeOpcodePatternMatcher(insn, type), priority);
            }
            return this;
        }

        public MethodVisitor build(MethodVisitor parent) {
            return this.builder.build(parent);
        }
    }
}
