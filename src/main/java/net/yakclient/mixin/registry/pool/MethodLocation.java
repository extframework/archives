package net.yakclient.mixin.registry.pool;

import net.yakclient.mixin.registry.MixinMetaData;

import java.util.Objects;

public class MethodLocation implements Location{
    private final Class<?> cls;
    private final String method;

    public MethodLocation(Class<?> cls, String method) {
        this.cls = cls;
        this.method = method;
    }

    public static MethodLocation fromDataDest(MixinMetaData data) {
        return new MethodLocation(data.getClassTo(), data.getMethodTo());
    }

    public static MethodLocation fromDataOrigin(MixinMetaData data) {
        return new MethodLocation(data.getClassFrom(), data.getMethodFrom());
    }

    public Class<?> getCls() {
        return cls;
    }

    public String getMethod() {
        return method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodLocation that = (MethodLocation) o;
        return Objects.equals(cls, that.cls) && Objects.equals(method, that.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cls, method);
    }
}
