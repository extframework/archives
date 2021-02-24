package net.yakclient.mixin.registry;

public interface Pointer {
    Class<?> retrieveClass(String cls) throws ClassNotFoundException;

    boolean classExists(String cls);

    ClassLoader classLoader();
}
