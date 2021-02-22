package net.yakclient.mixin.registry;

@FunctionalInterface
public interface FunctionalProxy {
    void accept(Runnable cancel);

    static ProxyResponseData run(FunctionalProxy proxy) {
        final ProxyResponseData.Builder data = new ProxyResponseData.Builder();

        /*
            When running the proxy the arguments are given to initialize the runnables which
            can be called at any time to cancel the event etc.
         */
        proxy.accept(() -> data.cancel(true));

        return data.build();
    }

    class ProxyResponseData {
        private final boolean cancel;

        public ProxyResponseData(boolean cancel) {
            this.cancel = cancel;
        }

        public static class Builder {
            private boolean cancel = false;

            public Builder() {
            }

            public Builder cancel(boolean cancel) {
                this.cancel = cancel;
                return this;
            }

            public ProxyResponseData build() {
                return new ProxyResponseData(this.cancel);
            }
        }
    }
}
