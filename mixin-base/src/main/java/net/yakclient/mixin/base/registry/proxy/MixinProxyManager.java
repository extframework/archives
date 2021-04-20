package net.yakclient.mixin.base.registry.proxy;

import net.yakclient.mixin.base.registry.FunctionalProxy;
import org.jetbrains.annotations.Contract;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MixinProxyManager {
    private final Map<UUID, FunctionalProxy> proxys;
    private static MixinProxyManager instance;

    private MixinProxyManager() {
        this.proxys = new HashMap<>();
    }

    private static synchronized MixinProxyManager getInstance() {
        if (instance == null) instance = new MixinProxyManager();
        return instance;
    }

    @Contract(pure = true)
    public static FunctionalProxy.ProxyResponseData proxy(UUID uuid) {
        final MixinProxyManager manager = getInstance();
        if (!manager.proxys.containsKey(uuid)) throw new IllegalArgumentException("Invalid UUID, no proxy found");
        return FunctionalProxy.run(manager.proxys.get(uuid));
    }

    public static void registerProxy(UUID uuid, FunctionalProxy proxy) {
        final MixinProxyManager manager = getInstance();

        if (manager.proxys.containsKey(uuid) && manager.proxys.get(uuid) != null)
            throw new IllegalArgumentException("Cannot override existing proxy's. Your UUID's have matched!");

        manager.proxys.put(uuid, proxy);
    }

    public static UUID register() {
        final var key = UUID.randomUUID();
        getInstance().proxys.put(key, null);
        return key;
    }
}
