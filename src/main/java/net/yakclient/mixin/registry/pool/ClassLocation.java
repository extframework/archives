package net.yakclient.mixin.registry.pool;

import net.yakclient.mixin.registry.MixinMetaData;

import java.util.Objects;

public class ClassLocation implements Location {
    private final Class<?> cls;

    public ClassLocation(Class<?> cls) {
        this.cls = cls;
    }

    public static ClassLocation fromDataDest(MixinMetaData data) {
        return new ClassLocation(data.getClassTo());
    }

    public static ClassLocation fromDataOrigin(MixinMetaData data) {
        return new ClassLocation(data.getClassFrom());
    }

    public Class<?> getCls() {
        return cls;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassLocation that = (ClassLocation) o;
        return Objects.equals(cls, that.cls);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cls);
    }
}
