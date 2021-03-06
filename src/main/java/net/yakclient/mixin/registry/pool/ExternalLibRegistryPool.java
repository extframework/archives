package net.yakclient.mixin.registry.pool;

import net.yakclient.mixin.internal.loader.PackageTarget;

import java.net.URL;

public class ExternalLibRegistryPool extends RegistryPool<URL> {
    @Override
    public Location pool(URL type) {
        final ExternalLibLocation key = new ExternalLibLocation(type);
        if (!this.pool.containsKey(key)) this.pool.put(key, new PoolQueue<>());

        this.pool.get(key).add(type, (t)->{}, null);

        return key;
    }

    @Override
    public PackageTarget register(Location location) {
        return null;
    }
}
