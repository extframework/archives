package net.yakclient.mixin.registry;

import java.util.Objects;

public class MixinMetaData {
    private final String classFrom;
    private final String methodFrom;
    private final String classTo;
    private final String methodTo;

    private final int type;
    private final int priority;

    public MixinMetaData(String classFrom,
                         String methodFrom,
                         String classTo,
                         String methodTo,
                         int type,
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

    public int getType() {
        return type;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MixinMetaData data = (MixinMetaData) o;
        return type == data.type && priority == data.priority && Objects.equals(classFrom, data.classFrom) && Objects.equals(methodFrom, data.methodFrom) && Objects.equals(classTo, data.classTo) && Objects.equals(methodTo, data.methodTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classFrom, methodFrom, classTo, methodTo, type, priority);
    }
}
