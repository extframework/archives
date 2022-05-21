package net.yakclient.bmu.api.extension

import org.objectweb.asm.tree.*
import java.util.function.Predicate

//public open class InsnListMatcher internal constructor(
//    private val toMatch: MutableList<InstructionMatcher> = ArrayList()
//) {
//    public companion object {
//        public const val IGNORE_FRAMES: Int = 0b0000_0001
//
//        public const val IGNORE_LABELS: Int = 0b0000_0010
//
//        public const val IGNORE_JUMPS: Int = 0b0000_0100
//
//        public const val IGNORE_LINE_NUMBERS: Int = 0b0000_1000
//    }
//
//    public open fun then(matcher: InstructionMatcher): InsnListMatcher = apply { toMatch.add(matcher) }
//
//    public open fun then(type: Class<out AbstractInsnNode>): InsnListMatcher = then(TypedInsnMatcher(type))
//
//    public open fun then(opcode: Int): InsnListMatcher = then(OpcodeInsnMatcher(opcode))
//
//    public open fun <T : AbstractInsnNode> then(type: Class<T>, predicate: Predicate<T>): InsnListMatcher =
//        then(TypedPredicateInsnMatcher(type, predicate))
//
//    @JvmOverloads
//    public fun find(
//        list: InsnList,
//        flags: Int = IGNORE_FRAMES or IGNORE_LABELS or IGNORE_JUMPS or IGNORE_LINE_NUMBERS
//    ): List<InsnList> {
//        fun Int.isSet(pos: Int): Boolean = this and (1 shl pos) != 0
//
//        var matching = toMatch
//        if (matching.isEmpty()) return ArrayList()
//
//        val found = ArrayList<AbstractInsnNode>()
//
//        for (node in list) {
//            if (
//                (flags.isSet(0) && node is FrameNode) ||
//                (flags.isSet(1) && node is LabelNode) ||
//                (flags.isSet(2) && node is JumpInsnNode) ||
//                (flags.isSet(3) && node is LineNumberNode)
//            ) continue
//
//            matching = if (matching.first().test(node)) matching.subList(1, matching.size) else toMatch
//            if (matching.isEmpty()) {
//                found.add(node)
//                matching = toMatch
//            }
//        }
//        return found
//    }
//}
//
//public inline fun <reified T: AbstractInsnNode> InsnListMatcher.then() : InsnListMatcher = then(T::class.java)
//
//public inline fun <reified T: AbstractInsnNode> InsnListMatcher.then(predicate: Predicate<T>): InsnListMatcher = then(T::class.java, predicate)