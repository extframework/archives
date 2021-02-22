package net.yakclient.mixin.registry.pool;

import net.yakclient.mixin.registry.Pointer;
import net.yakclient.mixin.registry.RegistryPointer;

import java.net.URL;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;

public class ExternalLibRegistryPool extends RegistryPool<URL> {
    @Override
    public CompletableFuture<Pointer> pool(URL type) {
        final ExternalLibLocation key = new ExternalLibLocation(type);
        if (!this.pool.containsKey(key)) this.pool.put(key, new PriorityQueue<>());

        this.pool.get(key).add(type);

        return new SuppliedFuture<>(() -> {
            this.register(this.pool.get(key);
            return new RegistryPointer()
        }));
    }

    @Override
    public void register(Queue<URL> queue) {

    }
}
