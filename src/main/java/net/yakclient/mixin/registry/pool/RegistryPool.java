package net.yakclient.mixin.registry.pool;

import net.yakclient.mixin.internal.loader.PackageTarget;

import java.util.*;
import java.util.function.Consumer;

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

//    public CompletableFuture<Pointer> retrieve(@NotNull Location location) {
//       return this.pool.get(location).future;
//    }

    static class PoolQueue<T> /* implements Iterable<T> */ {
        protected final Queue<PoolNode<T>> queue;
//        private final Runnable register;

        public PoolQueue() {
//            this.register = register;
//            this.queue = new LinkedList<>();
            this.queue = new LinkedList<>();
        }



//        public void register() {
//            this.register.run();
//        }


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
//
//        /*
//            This is a destructive iterator in that all items will be removed from the Queue.
//         */
//        @NotNull
//        @Override
//        public Iterator<T> iterator() {
//            return new PoolQueueIterator(this.queue);
//        }
//
//        private class PoolQueueIterator implements Iterator<T> {
//            private final Queue<T> queue;
//
//            public PoolQueueIterator(Queue<T> queue) {
//                this.queue = queue;
//            }
//
//            @Override
//            public boolean hasNext() {
//                return !this.queue.isEmpty();
//            }
//
//            @Override
//            public T next() {
//                return queue.poll();
//            }
//        }
    }
}
