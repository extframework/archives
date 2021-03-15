package net.yakclient.mixin.registry.pool;

import net.yakclient.mixin.internal.loader.PackageTarget;

import java.util.*;
import java.util.function.Consumer;

public abstract class RegistryPool<T> {
    protected final Map<Location, PoolQueue<T>> pool;

    public RegistryPool() {
        this.pool = new HashMap<>();
    }

    public abstract Location pool(T type) throws ClassNotFoundException;

    public abstract PackageTarget register(Location location);

    public void empty(Location location) {

        if (this.pool.containsKey(location)) this.register(location);
    }

    public void empty() {
        for (Location location : this.pool.keySet()) {
            this.register(location);
        }
    }

    static class PoolQueue<T> {
        protected final Queue<PoolNode<T>> queue;

        public PoolQueue() {
            this.queue = new LinkedList<>();
        }

        public PoolQueue<T> add(T type, Consumer<PackageTarget> registration) {
            this.queue.add(new PoolNode<>(type, registration));
            return this;
        }

        public PoolQueue<T> add(T type, Consumer<PackageTarget> registration, UUID proxy) {
            this.queue.add(new ProxiedPoolNode<>(type, registration, proxy));
            return this;
        }

        public static class PoolNode<T> {
            private final T value;
            private final Consumer<PackageTarget> registration;

            PoolNode(T value, Consumer<PackageTarget> registration) {
                this.value = value;
                this.registration = registration;
            }

            public T getValue() {
                return this.value;
            }

            public void run(PackageTarget target) {
                this.registration.accept(target);
            }
        }

        public static class ProxiedPoolNode<T> extends PoolNode<T> {
            private final UUID proxy;

            ProxiedPoolNode(T value, Consumer<PackageTarget> registration, UUID proxy) {
                super(value, registration);
                this.proxy = proxy;
            }

            public UUID getProxy() {
                return proxy;
            }
        }
    }
}
