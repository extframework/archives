package net.yakclient.mixin.api.versionadapter;

import org.jetbrains.annotations.Contract;

import java.util.HashMap;
import java.util.Map;

public class CollatedVMapping {
    private final String target;
    private final Map<String, Map<String, ClassVersion>> versions;

    public CollatedVMapping(String target) {
        this.target = target;
        this.versions = new HashMap<>();
    }

    public static CollatedVMapping createTargeted(String target, VersionMapping mapping) {
        return new CollatedVMapping(target).collate(mapping);
    }

    public CollatedVMapping collate(VersionMapping mapping) {
        if (!mapping.getTarget().equals(this.target))
            throw new IllegalArgumentException("Cannot collate mapping with a different target!");
        mapping.getClsVersions().forEach((key, value) -> {
            if (!this.versions.containsKey(key)) this.versions.put(key, new HashMap<>());
            this.versions.get(key).putAll(value);

        });
        return this;
    }

    @Contract(pure = true)
    public boolean hasMapping(String key, String version) {
        if (!this.versions.containsKey(key)) return false;
        return this.versions.get(key).containsKey(version);
    }

    @Contract(pure = true)
    public ClassVersion retrieve(String key, String version) {
        if (!this.hasMapping(key, version)) throw new IllegalArgumentException("Failed to find mapping");
        return this.versions.get(key).get(version);
    }

}
