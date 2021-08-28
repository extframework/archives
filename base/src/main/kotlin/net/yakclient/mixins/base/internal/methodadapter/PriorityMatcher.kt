package net.yakclient.mixins.base.internal.methodadapter

data class PriorityMatcher<T : MixinPatternMatcher?>(
    private val priority: Int,
    val patternMatcher: T
) : Comparable<PriorityMatcher<*>> {
    override fun compareTo(other: PriorityMatcher<*>): Int {
        return priority - other.priority
    }

}