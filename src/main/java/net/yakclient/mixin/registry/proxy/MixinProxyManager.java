package net.yakclient.mixin.registry.proxy;

import net.yakclient.mixin.registry.FunctionalProxy;
import net.yakclient.mixin.registry.PointerManager;
import org.jetbrains.annotations.NotNull;

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
//TODO Example        return new FunctionalProxy.ProxyResponseData(false);
        final MixinProxyManager manager = getInstance();
        if (!manager.proxys.containsKey(uuid)) throw new IllegalArgumentException("Invalid UUID, no proxy found");
        return manager.proxys.get(uuid).accept();
    }

    public static ProxiedPointer registerProxy(@NotNull UUID uuid, FunctionalProxy proxy) {
        final MixinProxyManager manager = getInstance();

        if (manager.proxys.containsKey(uuid)) throw new IllegalArgumentException("Cannot override existing proxy's. Your UUID's have matched!");
        if (!PointerManager.hasPointer(uuid)) throw new IllegalArgumentException("To register a proxy the pointer must first be registered");

        return new ProxiedPointer(uuid, proxy);
    }
}
