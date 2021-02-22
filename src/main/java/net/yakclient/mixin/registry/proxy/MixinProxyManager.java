package net.yakclient.mixin.registry.proxy;

import com.sun.istack.internal.NotNull;
import net.yakclient.mixin.registry.FunctionalProxy;

import java.util.*;

public class MixinProxyManager {
    private final Map<UUID, ProxiedPointer> proxys;
    private static MixinProxyManager instance;

    private MixinProxyManager() {
        this.proxys = new HashMap<>();
    }

    private static synchronized MixinProxyManager getInstance() {
        if (instance == null) instance = new MixinProxyManager();
        return instance;
    }

    public static FunctionalProxy.ProxyResponseData proxy(@NotNull UUID uuid) {
        final MixinProxyManager manager = getInstance();
        if (!manager.proxys.containsKey(uuid)) throw new IllegalArgumentException("Invalid UUID, no proxy found");
        return manager.proxys.get(uuid).accept();
    }

    public static UUID registerProxy(@NotNull ProxiedPointer proxy) {
        final MixinProxyManager manager = getInstance();

        if (manager.proxys.containsKey(proxy.getUUID())) throw new IllegalArgumentException("Cannot override existing proxy's. Your UUID's have matched!");

        return Objects.requireNonNull(manager.proxys.put(proxy.getUUID(), proxy)).getUUID();
    }
}
