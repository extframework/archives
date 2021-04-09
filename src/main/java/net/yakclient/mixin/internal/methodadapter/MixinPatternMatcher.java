package net.yakclient.mixin.internal.methodadapter;

import net.yakclient.mixin.api.InjectionType;
import net.yakclient.mixin.internal.ASMType;
import net.yakclient.mixin.internal.instruction.Instruction;
import net.yakclient.mixin.internal.instruction.core.CoreInstruction;
import net.yakclient.mixin.internal.instruction.tree.TreeInstruction;
import net.yakclient.mixin.internal.methodadapter.core.*;
import net.yakclient.mixin.internal.methodadapter.tree.TreeABPatternMatcher;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.InsnList;

/**
 * Purely a marker interface however it can provide utility
 * functionality for matching to a certain PatternMatcher.
 *
 * @author Durgan McBroom
 */
public interface MixinPatternMatcher {
//    void transform(InsnList instructions);

    static <T extends Instruction> MixinPatternMatcher match(int type, MethodVisitor v, T insn) {
        return match(type, ASMType.TREE, v, insn);
    }

    @Nullable
    static <T extends Instruction> MixinPatternMatcher match(int type, ASMType asmType, MethodVisitor v, T insn) {
        if (asmType == ASMType.TREE) {
            final CoreInstruction coreInstruction = (CoreInstruction) insn;
            switch (type) {
                case InjectionType.AFTER_BEGIN:
                    return new CoreABPatternMatcher(v, coreInstruction);
                case InjectionType.BEFORE_INVOKE:
                    return new CoreBIPatternMatcher(v, coreInstruction);
                case InjectionType.BEFORE_RETURN:
                    return new CoreBRPatternMatcher(v, coreInstruction);
                case InjectionType.BEFORE_END:
                    return new CoreBEPatternMatcher(v, coreInstruction);
                case InjectionType.OVERWRITE:
                    return new CoreOverwritePatternMatcher(v, coreInstruction);
                default:
                    return new CoreOpcodeMatcherPattern(v, coreInstruction, type);
            }
        } else {
            final TreeInstruction treeInstruction = (TreeInstruction) insn;
            switch (type) {
                case InjectionType.AFTER_BEGIN:
                    return new TreeABPatternMatcher(treeInstruction);
            }
        }
        return null;
    }
}
