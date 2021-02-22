package net.yakclient.mixin.registry;

import net.yakclient.mixin.api.InjectionType;

public class MixinMetaData {
    private final Class<?> classFrom;
    private final String methodFrom;
    private final Class<?> classTo;
    private final String methodTo;

    private final InjectionType type;
    private final int priority;

    public MixinMetaData(Class<?> classFrom,
                         String methodFrom,
                         Class<?> classTo,
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

    public Class<?> getClassFrom() {
        return classFrom;
    }

    public String getMethodFrom() {
        return methodFrom;
    }

    public Class<?> getClassTo() {
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
}
