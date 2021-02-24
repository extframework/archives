package net.yakclient.mixin.registry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PointerManager {
    private final Map<UUID, ClassLoader> loaders;
    private static PointerManager instance;

    private PointerManager() {
        this.loaders = new HashMap<>();
    }

    private static synchronized PointerManager getInstance() {
        if (instance == null) instance = new PointerManager();
        return instance;
    }

    public static @NotNull UUID register(ClassLoader csl) {
        final UUID key = UUID.randomUUID();
        getInstance().loaders.put(key, csl);
        return key;
    }

    public static @NotNull UUID register() {
        final UUID key = UUID.randomUUID();
        getInstance().loaders.put(key, null);
        return key;
    }

    public static UUID overload(UUID uuid, ClassLoader csl) {
        getInstance().loaders.put(uuid, csl);
        return uuid;
    }

    @Nullable
    public static ClassLoader retrieve(UUID uuid) {
        return getInstance().loaders.get(uuid);
    }

    public static boolean hasPointer(UUID uuid) {
        return getInstance().loaders.containsKey(uuid);
    }
}
