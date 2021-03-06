package net.yakclient.mixin.registry.proxy;

import net.yakclient.mixin.registry.FunctionalProxy;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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
    public static FunctionalProxy.ProxyResponseData proxy(@NotNull UUID uuid) {
        return new FunctionalProxy.ProxyResponseData(false);

//        final MixinProxyManager manager = getInstance();
//        if (!manager.proxys.containsKey(uuid)) throw new IllegalArgumentException("Invalid UUID, no proxy found");
//        return FunctionalProxy.run(manager.proxys.get(uuid));
    }

    public static void registerProxy(@NotNull UUID uuid, FunctionalProxy proxy) {
        final MixinProxyManager manager = getInstance();

        if (manager.proxys.containsKey(uuid) && manager.proxys.get(uuid) != null)
            throw new IllegalArgumentException("Cannot override existing proxy's. Your UUID's have matched!");

        manager.proxys.put(uuid, proxy);
    }

    public static UUID register() {
        final UUID key = UUID.randomUUID();
        getInstance().proxys.put(key, null);
        return key;
    }
}
