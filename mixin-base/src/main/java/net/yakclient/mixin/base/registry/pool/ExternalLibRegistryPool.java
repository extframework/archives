package net.yakclient.mixin.base.registry.pool;

import net.yakclient.mixin.base.internal.loader.PackageTarget;

import java.net.URL;

public class ExternalLibRegistryPool extends RegistryPool<URL> {
    @Override
    public Location pool(URL type) {
        final var key = new ExternalLibLocation(type);
        if (!this.pool.containsKey(key)) this.pool.put(key, new PoolQueue<>());

        this.pool.get(key).add(type, null);

        return key;
    }

    @Override
    public PackageTarget register(Location location) {
        return null;
    }
}
