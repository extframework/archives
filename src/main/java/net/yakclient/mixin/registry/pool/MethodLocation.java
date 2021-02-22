package net.yakclient.mixin.registry.pool;

import net.yakclient.mixin.api.InjectionType;
import net.yakclient.mixin.registry.MixinMetaData;

import java.util.Objects;

public class MethodLocation implements Location {
    private final Class<?> cls;
    private final String method;
    private final InjectionType injectionType;
    private final int priority;

    public MethodLocation(Class<?> cls, String method, InjectionType injectionType, int priority) {
        this.cls = cls;
        this.method = method;
        this.injectionType = injectionType;
        this.priority = priority;
    }

    public MethodLocation(MixinMetaData data) {
        this.cls = data.getClassTo();
        this.method = data.getMethodTo();
        this.injectionType = data.getType();
        this.priority = data.getPriority();
    }

    public Class<?> getCls() {
        return cls;
    }

    public String getMethod() {
        return method;
    }

    public InjectionType getInjectionType() {
        return injectionType;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodLocation that = (MethodLocation) o;
        return priority == that.priority && Objects.equals(cls, that.cls) && Objects.equals(method, that.method) && injectionType == that.injectionType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cls, method, injectionType, priority);
    }
}
