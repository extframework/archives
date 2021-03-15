package net.yakclient.mixin.registry;

import net.yakclient.mixin.api.InjectionType;

import java.util.Objects;

public class MixinMetaData {
    private final String classFrom;
    private final String methodFrom;
    private final String classTo;
    private final String methodTo;

    private final InjectionType type;
    private final int priority;

    public MixinMetaData(String classFrom,
                         String methodFrom,
                         String classTo,
                         String methodTo,
                         InjectionType type,
                         int priority) {
        this.classFrom = classFrom;
        this.methodFrom = methodFrom;
        this.classTo = classTo;
        this.methodTo = methodTo;
        this.type = type;
        this.priority = priority;
    }

    public String getClassFrom() {
        return classFrom;
    }

    public String getMethodFrom() {
        return methodFrom;
    }

    public String getClassTo() {
        return classTo;
    }

    public String getMethodTo() {
        return methodTo;
    }

    public InjectionType getType() {
        return type;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MixinMetaData that = (MixinMetaData) o;
        return priority == that.priority && Objects.equals(classFrom, that.classFrom) && Objects.equals(methodFrom, that.methodFrom) && Objects.equals(classTo, that.classTo) && Objects.equals(methodTo, that.methodTo) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(classFrom, methodFrom, classTo, methodTo, type, priority);
    }
}
