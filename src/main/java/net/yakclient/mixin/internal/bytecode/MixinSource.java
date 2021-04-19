package net.yakclient.mixin.internal.bytecode;

import net.yakclient.mixin.registry.pool.QualifiedMethodLocation;

import java.util.Objects;
import java.util.UUID;

public class MixinSource {
    private final QualifiedMethodLocation location;

    public MixinSource(QualifiedMethodLocation location) {
        this.location = location;
    }

    public QualifiedMethodLocation getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "MixinSource{" +
                "location=" + location +
                '}';
    }

    public static class MixinProxySource extends MixinSource {
        private final UUID pointer;

        public MixinProxySource(QualifiedMethodLocation location, UUID pointer) {
            super(location);
            this.pointer = pointer;
        }

        public UUID getPointer() {
            return pointer;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final var that = (MixinProxySource) o;
            return Objects.equals(pointer, that.pointer);
        }

        @Override
        public int hashCode() {
            return Objects.hash(pointer);
        }

        @Override
        public String toString() {
            return "MixinProxySource{" +
                    "pointer=" + pointer +
                    '}';
        }
    }
}
