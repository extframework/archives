package net.yakclient.mixin.registry;

import org.jetbrains.annotations.Contract;

import java.util.Objects;
import java.util.UUID;

public class RegistryPointer implements Pointer {
    private final UUID uuid;

    public RegistryPointer(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return uuid;
    }

    @Override
    public Class<?> retrieveClass(String cls) throws ClassNotFoundException {
        if (!PointerManager.hasPointer(this.uuid)) throw new IllegalStateException("Pointers must be registered");
        return PointerManager.retrieve(this.uuid).loadClass(cls);
    }

    @Override
    @Contract(pure = true)
    public boolean classExists(String cls) {
        try {
            retrieveClass(cls);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public ClassLoader classLoader() {
        if (!PointerManager.hasPointer(this.uuid)) throw new IllegalStateException("Pointers must be registered");
        return PointerManager.retrieve(this.uuid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistryPointer that = (RegistryPointer) o;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
