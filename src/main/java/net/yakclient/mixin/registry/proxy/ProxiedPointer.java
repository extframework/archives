package net.yakclient.mixin.registry.proxy;

import net.yakclient.mixin.registry.FunctionalProxy;
import net.yakclient.mixin.registry.RegistryPointer;

import java.util.Objects;
import java.util.UUID;

public class ProxiedPointer extends RegistryPointer {
    private final FunctionalProxy proxy;

    ProxiedPointer(UUID uuid, FunctionalProxy proxy) {
        super(uuid);

        this.proxy = proxy;
    }

    public FunctionalProxy.ProxyResponseData accept() {
        return FunctionalProxy.run(this.proxy);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ProxiedPointer that = (ProxiedPointer) o;
        return Objects.equals(proxy, that.proxy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), proxy);
    }
}
