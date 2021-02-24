package net.yakclient.mixin.registry;

import net.yakclient.mixin.api.Injection;
import net.yakclient.mixin.api.Mixer;
import net.yakclient.mixin.registry.pool.ExternalLibRegistryPool;
import net.yakclient.mixin.registry.pool.Location;
import net.yakclient.mixin.registry.pool.MixinRegistryPool;
import net.yakclient.mixin.registry.pool.RegistryPool;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class MixinRegistry {
    private final RegistryConfigurator.Configuration configuration;
    private final ExternalLibRegistryPool libRegistry;
    private final MixinRegistryPool mixinRegistry;

    MixinRegistry(RegistryConfigurator.Configuration configuration) {
        this.configuration = configuration;
        this.libRegistry = new ExternalLibRegistryPool();
        this.mixinRegistry = new MixinRegistryPool();
    }

    //TODO there needs to be a better way to have the mixin pool get dumped. Currently nothing will happen if one of our calls is not invoked. A solution could be spawning off another thread and then having a callback? For external libs its very simple and will just go when its needed.

    /**
     * Registers a Mixin from the given class.
     *
     * Right now it doest make sense to return a pointer after adding the mixin.
     * there would be virtually nothing you would need to do with it and accessing
     * the class would already be provided by calling the default classloader etc.
     *
     *
     * @param cls The class to take mixins from
     * @return MixinRegistry for easy access.
     */
    public MixinRegistry registerMixin(Class<?> cls) {
        final Set<MixinMetaData> data = this.data(cls);

        for (MixinMetaData datum : data) this.mixinRegistry.pool(datum);

        return this;
    }

    public MixinRegistry registerMixin(Class<?> cls, FunctionalProxy proxy) {
        final Set<MixinMetaData> data = this.data(cls);

        for (MixinMetaData datum : data) this.mixinRegistry.pool(datum, proxy);

        return this;
    }

    private Set<MixinMetaData> data(Class<?> cls) {
        //TODO redo this eventually
        if (!cls.isAnnotationPresent(Mixer.class))
            throw new IllegalArgumentException("Mixins must be annotated with @Mixer");
        final Class<?> type = cls.getAnnotation(Mixer.class).value();

        Set<MixinMetaData> mixins = new HashSet<>();
        for (Method method : cls.getMethods()) {
            if (method.isAnnotationPresent(Injection.class)) {
                final Injection injection = method.getAnnotation(Injection.class);
                final String methodTo = this.mixinToMethodName(method);

                if (!this.declaredMethod(type, methodTo, method.getParameterTypes()))
                    throw new IllegalArgumentException("Failed to find a appropriate method to mix to");

                mixins.add(new MixinMetaData(cls,
                        method.getName(),
                        type,
                        methodTo, injection.type(), injection.priority()));
            }
        }
        return mixins;
    }

    private String mixinToMethodName(Method method) {
        if (!method.isAnnotationPresent(Injection.class)) return method.getName();
        final Injection injection = method.getAnnotation(Injection.class);
        return injection.to().equals(Injection.METHOD_SELF) ? method.getName() : injection.to();
    }

    private boolean declaredMethod(Class<?> cls, String method, Class<?>... parameterTypes) {
        try {
            cls.getMethod(method, parameterTypes);
        } catch (NoSuchMethodException e) {
            return false;
        }
        return true;
    }

    public void dumpAll() {
        this.libRegistry.empty();
        this.mixinRegistry.empty();
    }

//    private CompletableFuture<Pointer> registerMixin(MixinMetaData)


//    public ClassLoader retrieveLoader(UUID)
}
