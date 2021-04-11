package net.yakclient.mixin.internal.methodadapter;

import org.jetbrains.annotations.NotNull;

public class PriorityMatcher<T extends MixinPatternMatcher> implements Comparable<PriorityMatcher<?>> {
    private final int priority;
    private final T pm;

    public PriorityMatcher(int priority, T pm) {
        this.priority = priority;
        this.pm = pm;
    }

    @Override
    public int compareTo(@NotNull PriorityMatcher<?> o) {
        return this.priority - o.priority;
    }

    public T get() {
        return pm;
    }
}
