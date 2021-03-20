package net.yakclient.mixin.versionadapter;

import java.util.HashMap;
import java.util.Map;

public class VersionMapping {
    private final String target;
    private final Map<String, Map<String, ClassVersion>> clsVersions;

    public VersionMapping(MappingsBuilder builder) {
        this.target = builder.target;
        this.clsVersions = builder.clsTargets;
    }

    public String getTarget() {
        return target;
    }

    Map<String, Map<String, ClassVersion>> getClsVersions() {
        return clsVersions;
    }

    public static class MappingsBuilder {
        private final String target;
        private final Map<String, Map<String, ClassVersion>> clsTargets;

        public MappingsBuilder(String target) {
            this.target = target;
            this.clsTargets = new HashMap<>();
        }

        public VersionsBuilder clsTargets(String target) {
            return new VersionsBuilder(target, this);
        }

        public MappingsBuilder clsTargets(String target, VersionsBuilder builder) {
            this.clsTargets.put(target, builder.versions);
            return this;
        }

        public MappingsBuilder clsTargets(String target, Map<String, ClassVersion> versions) {
            this.clsTargets.put(target, versions);
            return this;
        }

        public VersionMapping build() {
            return new VersionMapping(this);
        }

        public static class VersionsBuilder {
            private final Map<String, ClassVersion> versions;
            private final String target;
            private final MappingsBuilder builder;

            private VersionsBuilder(String target, MappingsBuilder builder) {
                this.target = target;
                this.builder = builder;
                this.versions = new HashMap<>();
            }

            public VersionsBuilder version(String version, ClassVersion to) {
                this.versions.put(version, to);
                return this;
            }

            public MappingsBuilder build() {
                return this.builder.clsTargets(this.target, this);
            }
        }
    }
}
