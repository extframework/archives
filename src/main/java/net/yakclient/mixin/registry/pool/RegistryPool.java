package net.yakclient.mixin.registry.pool;

import net.yakclient.mixin.registry.Pointer;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;

public abstract class RegistryPool<T> {
    protected final Map<Location, Queue<T>> pool;

    public RegistryPool() {
        this.pool = new HashMap<>();
    }

    public abstract CompletableFuture<Pointer> pool(T type);

    public abstract void register(Queue<T> queue);

    public void empty(Location location) {
        final Queue<T> queue = this.pool.get(location);

        if (queue != null) this.register(queue);
    }

    public void empty() {
        for (Location location : this.pool.keySet()) {
            this.register(this.pool.get(location));
        }
    }
}
