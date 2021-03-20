package net.yakclient.mixin.versionadapter;

import java.util.HashMap;
import java.util.Map;

public class ClassVersion {
    private final String clazz;
    private final Map<String, String> methods;
    private final Map<String, String> fields;

    public ClassVersion(String clazz, Map<String, String> methods, Map<String, String> fields) {
        this.clazz = clazz;
        this.methods = methods;
        this.fields = fields;
    }

    public String getMethod(String identifier) {
        return this.methods.get(identifier);
    }


    public String getField(String identifier) {
        return this.fields.get(identifier);
    }

//    public void iterateMethods(Consumer<String> consumer) {
//        for (String method : this.methods) consumer.accept(method);
//    }
//
//    public void iterateFields(Consumer<String> consumer) {
//        for (String field : this.fields) consumer.accept(field);
//    }

    public String getClazz() {
        return clazz;
    }

    public static class ClassVersionBuilder {
        private final String clazz;
        private final Map<String, String> methods;
        private final Map<String, String> fields;

        public ClassVersionBuilder(String clazz) {
            this.clazz = clazz;
            this.methods = new HashMap<>();
            this.fields = new HashMap<>();
        }

        public ClassValueBuilder buildMethods() {
            final ClassVersionBuilder builder = this;
            return new ClassValueBuilder() {
                @Override
                public ClassVersionBuilder build() {
                    builder.methods.putAll(this.value);
                    return builder;
                }
            };
        }

        public ClassValueBuilder buildFields() {
            final ClassVersionBuilder builder = this;
            return new ClassValueBuilder() {
                @Override
                public ClassVersionBuilder build() {
                    builder.fields.putAll(this.value);
                    return builder;
                }
            };
        }

        public ClassVersion build() {
            return new ClassVersion(
                    this.clazz,
                    this.methods,
                    this.fields);
        }

        public abstract static class ClassValueBuilder {
            Map<String, String> value;

            private ClassValueBuilder() {
                this.value = new HashMap<>();
            }

            public ClassValueBuilder add(String key, String value) {
                this.value.put(key, value);
                return this;
            }

            public ClassValueBuilder addAll(Map<String, String> values) {
                this.value.putAll(values);
                return this;
            }

            public abstract ClassVersionBuilder build();
        }
    }
}
