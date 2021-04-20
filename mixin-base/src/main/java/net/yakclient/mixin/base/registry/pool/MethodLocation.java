package net.yakclient.mixin.base.registry.pool;

import java.util.Objects;

public class MethodLocation extends ClassLocation {
    private final String method;

    public MethodLocation(String cls, String method) {
        super(cls);
        this.method = method;
    }

    public static MethodLocation fromDataDest(MixinMetaData data) {
        return new MethodLocation(data.getClassTo(), data.getMethodTo());
    }

    public static MethodLocation fromDataOrigin(MixinMetaData data) {
        return new MethodLocation(data.getClassFrom(), data.getMethodFrom());
    }


    public String getMethod() {
        return method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        final var that = (MethodLocation) o;
        return Objects.equals(method, that.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), method);
    }
}
