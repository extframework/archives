package net.yakclient.mixin.base.registry.pool;

import java.util.Objects;

public class QualifiedMethodLocation extends MethodLocation {
    private final int injectionType;
    private final int priority;

    public QualifiedMethodLocation(String cls, String method, int injectionType, int priority) {
        super(cls, method);
        this.injectionType = injectionType;
        this.priority = priority;
    }

    public static QualifiedMethodLocation fromDataDest(MixinMetaData data) {
        return new QualifiedMethodLocation(data.getClassTo(), data.getMethodTo(), data.getType(), data.getPriority());
    }

    public static QualifiedMethodLocation fromDataOrigin(MixinMetaData data) {
        return new QualifiedMethodLocation(data.getClassFrom(), data.getMethodFrom(), data.getType(), data.getPriority());
    }


    public int getInjectionType() {
        return injectionType;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final var that = (QualifiedMethodLocation) o;
        return priority == that.priority && injectionType == that.injectionType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(injectionType, priority);
    }
}
