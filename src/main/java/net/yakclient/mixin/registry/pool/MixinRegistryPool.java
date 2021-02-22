package net.yakclient.mixin.registry.pool;

import net.yakclient.mixin.registry.MixinMetaData;
import net.yakclient.mixin.registry.Pointer;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;

public class MixinRegistryPool extends RegistryPool<MixinMetaData> {
    @Override
    public CompletableFuture<Pointer> pool(MixinMetaData type) {
        final MethodLocation key = new MethodLocation(type);
        if (!this.pool.containsKey(key)) this.pool.put(key, new PriorityQueue<>());

        this.pool.get(key).add(type);

        return new SuppliedFuture<Pointer>(() -> {
            this.register(this.pool.get(key));

        });

    }

    @Override
    public void register(Queue<MixinMetaData> queue) {

    }
}
