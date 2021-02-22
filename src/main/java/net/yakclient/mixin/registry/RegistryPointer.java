package net.yakclient.mixin.registry;

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
    public Class<?> retrieveClass(String cls) {
        return null;
    }

    @Override
    public boolean classExists(String cls) {
        return false;
    }

    @Override
    public ClassLoader classLoader() {
        return null;
    }
}
