package net.yakclient.mixin.internal.loader;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

//TODO pretty sure i dont need this
public class ClassManager {
    private final Map<String, ClassLoader> overloads;
    private final ClassLoader parent;

    private static ClassManager instance;

    private ClassManager() {
        this.overloads = new HashMap<>();
        this.parent = getClass().getClassLoader();
    }

    @Nullable
    public static Class<?> retrieve(String cls) {
        final ClassManager instance = getInstance();
        try {
            if (instance.overloads.containsKey(cls))
                return instance.overloads.get(cls).loadClass(cls);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Classloader MUST provide a implementation of all overloads.");
        }
        return null;
    }

    public static ClassLoader applyOverload(String cls, ProxyClassLoader csl) {
        if (!csl.isDefined(cls))
            throw new IllegalArgumentException("Class loader MUST provide a overriding implementation of: " + cls);
        return getInstance().overloads.put(cls, csl);
    }

    public static ClassLoader applyOverload(Class<?> cls) {
        return getInstance().overloads.put(cls.getName(), cls.getClassLoader());
    }

    public static boolean hasOverload(String cls) {
        return getInstance().overloads.containsKey(cls);
    }

    public static ClassLoader parentLoader() {
        return getInstance().parent;
    }

    private static synchronized ClassManager getInstance() {
        if (instance == null) return instance = new ClassManager();
        return instance;
    }
}
