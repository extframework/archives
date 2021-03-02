package net.yakclient.mixin.registry.pool;

import net.yakclient.mixin.internal.loader.PackageTarget;
import net.yakclient.mixin.registry.Pointer;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public abstract class RegistryPool<T> {
    protected final Map<Location, PoolQueue<T>> pool;

    public RegistryPool() {
        this.pool = new HashMap<>();
    }

    public abstract Location pool(T type);



    public abstract PackageTarget register(Location location);

    public void empty(Location location) {

        if (this.pool.containsKey(location)) this.register(location);
    }

    public void empty() {
        for (Location location : this.pool.keySet()) {
            this.register(location);
        }
    }

    public CompletableFuture<Pointer> retrieve(@NotNull Location location) {
       return this.pool.get(location).future;
    }

    static class PoolQueue<T> implements Iterable<T> {
        protected final Queue<T> queue;
        private final CompletableFuture<Pointer> future;

        public PoolQueue(CompletableFuture<Pointer> future) {
            this.queue = new PriorityQueue<>();
            this.future = future;
        }

        public CompletableFuture<Pointer> getFuture() {
            return future;
        }

        public PoolQueue<T> add(T type) {
            this.queue.add(type);
            return this;
        }

        /*
            This is a destructive iterator in that all items will be removed from the Queue.
         */
        @NotNull
        @Override
        public Iterator<T> iterator() {
            return new PoolQueueIterator(this.queue);
        }

        private class PoolQueueIterator implements Iterator<T> {
            private final Queue<T> queue;

            public PoolQueueIterator(Queue<T> queue) {
                this.queue = queue;
            }

            @Override
            public boolean hasNext() {
                return !this.queue.isEmpty();
            }

            @Override
            public T next() {
                return queue.poll();
            }
        }
    }
}
