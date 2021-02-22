package net.yakclient.mixin.registry;

public interface Pointer {
    Class<?> retrieveClass(String cls);

    boolean classExists(String cls);

    ClassLoader classLoader();
}
