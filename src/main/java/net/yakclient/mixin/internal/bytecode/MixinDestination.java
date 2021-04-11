package net.yakclient.mixin.internal.bytecode;

import java.util.HashSet;
import java.util.Set;

public class MixinDestination {
    private final String method;
    private final MixinSource[] sources;

    public MixinDestination(MixinDestBuilder builder) {
        this.method = builder.method;
        this.sources = builder.sources.toArray(new MixinSource[0]);
    }

    public String getMethod() {
        return method;
    }

    public MixinSource[] getSources() {
        return sources;
    }

    public static class MixinDestBuilder {
        private final String method;
        private final Set<MixinSource> sources;

        public MixinDestBuilder(String method) {
            this.method = method;
            this.sources = new HashSet<>();
        }

        public MixinDestBuilder addSource(MixinSource source) {
            this.sources.add(source);
            return this;
        }

        public MixinDestination build() {
            return new MixinDestination(this);
        }
    }
}
