package net.yakclient.mixin.internal.bytecode;

import java.util.HashSet;
import java.util.Set;

public class MixinDestination {
    private final String method;
    private final Set<MixinSource> sources;

    public MixinDestination(String method) {
        this.method = method;
        this.sources = new HashSet<>();
    }

    public MixinDestination addSource(MixinSource source) {
        this.sources.add(source);
        return this;
    }


    public String getMethod() {
        return method;
    }

    public MixinSource[] getSources() {
        return sources.toArray(new MixinSource[0]);
    }

    public MixinDestination(String method, Set<MixinSource> sources) {
        this.method = method;
        this.sources = sources;
    }
}
