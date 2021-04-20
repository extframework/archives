package net.yakclient.mixin.base.registry.pool;

import java.util.Objects;

public class ClassLocation implements Location {
    private final String cls;

    public ClassLocation(String cls) {
        this.cls = cls;
    }

    public static ClassLocation fromDataDest(MixinMetaData data) {
        return new ClassLocation(data.getClassTo());
    }

    public static ClassLocation fromDataOrigin(MixinMetaData data) {
        return new ClassLocation(data.getClassFrom());
    }

    public String getCls() {
        return cls;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final var that = (ClassLocation) o;
        return Objects.equals(cls, that.cls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cls);
    }
}
