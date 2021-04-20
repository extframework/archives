package net.yakclient.mixin.base.registry.pool;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class SuppliedFuture<T> extends CompletableFuture<T> {
    private final Supplier<T> supplier;

    public SuppliedFuture(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public T get() {
        if (!this.isDone()) this.complete(this.supplier.get());
        return this.supplier.get();
    }
}
