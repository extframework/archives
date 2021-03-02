package net.yakclient.mixin.registry;

import net.yakclient.mixin.internal.loader.PackageTarget;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PointerManager {
    private final Map<UUID, PackageTarget> loaders;
    private static PointerManager instance;

    private PointerManager() {
        this.loaders = new HashMap<>();
    }

    private static synchronized PointerManager getInstance() {
        if (instance == null) instance = new PointerManager();
        return instance;
    }

    public static @NotNull UUID register(PackageTarget target) {
        final UUID key = UUID.randomUUID();
        getInstance().loaders.put(key, target);
        return key;
    }

    public static @NotNull UUID register() {
        final UUID key = UUID.randomUUID();
        getInstance().loaders.put(key, null);
        return key;
    }

    public static UUID overload(UUID uuid, PackageTarget target) {
        getInstance().loaders.put(uuid, target);
        return uuid;
    }

    @Nullable
    public static PackageTarget retrieve(UUID uuid) {
        return getInstance().loaders.get(uuid);
    }

    public static boolean hasPointer(UUID uuid) {
        return getInstance().loaders.containsKey(uuid);
    }
}
