package net.yakclient.mixin.registry.proxy;

import net.yakclient.mixin.registry.FunctionalProxy;
import net.yakclient.mixin.registry.RegistryPointer;

import java.util.UUID;

public class ProxiedPointer extends RegistryPointer {
    private final FunctionalProxy proxy;

    protected ProxiedPointer(UUID uuid, FunctionalProxy proxy) {
        super(uuid);
        this.proxy = proxy;
    }

    public FunctionalProxy.ProxyResponseData accept() {
        return FunctionalProxy.run(this.proxy);
    }
}
