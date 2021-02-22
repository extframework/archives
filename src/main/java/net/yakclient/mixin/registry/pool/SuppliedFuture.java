package net.yakclient.mixin.registry.pool;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

public class SuppliedFuture<T> extends CompletableFuture<T> {
    private final Supplier<T> supplier;

    public SuppliedFuture(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        if (!this.isDone()) this.complete(this.supplier.get());
        return super.get();
    }
}
