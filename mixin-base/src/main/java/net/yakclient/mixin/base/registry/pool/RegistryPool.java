package net.yakclient.mixin.base.registry.pool;

import net.yakclient.mixin.base.internal.loader.PackageTarget;

import java.util.*;

public abstract class RegistryPool<T> {
    protected final Map<Location, PoolQueue<T>> pool;

    public RegistryPool() {
        this.pool = new HashMap<>();
    }

    public abstract Location pool(T type) throws ClassNotFoundException;

    public abstract PackageTarget register(Location location);

    public void registerAll(Location location) {
        if (this.pool.containsKey(location)) this.register(location);
    }

    public void registerAll() {
        for (Location location : this.pool.keySet()) {
            this.register(location);
        }
    }

    static class PoolQueue<T> {
        protected final Queue<PoolNode<T>> queue;

        public PoolQueue() {
            this.queue = new LinkedList<>();
        }

        public PoolQueue<T> add(T type) {
            this.queue.add(new PoolNode<>(type));
            return this;
        }

        public PoolQueue<T> add(T type, UUID proxy) {
            this.queue.add(new ProxiedPoolNode<>(type, proxy));
            return this;
        }

        public static class PoolNode<T> {
            private final T value;

            PoolNode(T value) {
                this.value = value;
            }

            public T getValue() {
                return this.value;
            }
        }

        public static class ProxiedPoolNode<T> extends PoolNode<T> {
            private final UUID proxy;

            ProxiedPoolNode(T value, UUID proxy) {
                super(value);
                this.proxy = proxy;
            }

            public UUID getProxy() {
                return proxy;
            }
        }
    }
}
