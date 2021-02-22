package net.yakclient.mixin.registry;


import com.sun.istack.internal.Nullable;
import org.jetbrains.annotations.NotNull;

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

    public static @NotNull
    UUID register(ClassLoader csl) {
        final UUID key = UUID.randomUUID();
        getInstance().loaders.put(key, csl);
        return key;
    }

    @Nullable
    public static ClassLoader retrieve(UUID uuid) {
        return getInstance().loaders.get(uuid);
    }
}
