package net.yakclient.mixin.registry;

import net.yakclient.mixin.api.Injection;
import net.yakclient.mixin.api.Mixer;
import net.yakclient.mixin.internal.bytecode.ByteCodeUtils;
import net.yakclient.mixin.internal.loader.ClassTarget;
import net.yakclient.mixin.internal.loader.ContextPoolManager;
import net.yakclient.mixin.internal.loader.PackageTarget;
import net.yakclient.mixin.registry.pool.*;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class MixinRegistry {
    private final RegistryConfigurator.Configuration configuration;
    private final ExternalLibRegistryPool libRegistry;
    private final MixinRegistryPool mixinRegistry;

    private MixinRegistry(RegistryConfigurator.Configuration configuration) {
        this.configuration = configuration;
        this.libRegistry = new ExternalLibRegistryPool();
        this.mixinRegistry = new MixinRegistryPool();
    }

    public static MixinRegistry create(RegistryConfigurator.Configuration configuration) {
        for (PackageTarget target : configuration.targets)
            ContextPoolManager.applyTarget(target);

        return new MixinRegistry(configuration);
    }


    //TODO there needs to be a better way to have the mixin pool get dumped. Currently nothing will happen if one of our calls is not invoked. A solution could be spawning off another thread and then having a callback? For external libs its very simple and will just go when its needed.
    //TODO there are alot of issues with class reloading for example if a mixin class gets loaded(which it by definition has to) then it loads all of the classes it might reference in parameters etc. so we could fix this by not allowing the loading of mixin classes? that might be an option.

    /**
     * Registers a Mixin from the given class.
     * <p>
     * Right now it doest make sense to return a pointer after adding the mixin.
     * there would be virtually nothing you would need to do with it and accessing
     * the class would already be provided by calling the default classloader etc.
     *
     * @param cls The class to take mixins from
     * @return MixinRegistry for easy access.
     */
    public MixinRegistry registerMixin(Class<?> cls) throws ClassNotFoundException {
        final Set<MixinMetaData> data = this.data(cls);

        for (MixinMetaData datum : data) this.mixinRegistry.pool(datum);

        return this;
    }

    public MixinRegistry registerMixin(Class<?> cls, FunctionalProxy proxy) throws ClassNotFoundException {
        final Set<MixinMetaData> data = this.data(cls);

        for (MixinMetaData datum : data) this.mixinRegistry.pool(datum, proxy);

        return this;
    }

    private void validateMixin(String cls) {
        RegistryConfigurator.safeTarget(this.configuration, ClassTarget.create(cls).toPackage());
    }


    private Set<MixinMetaData> data(Class<?> cls) {
        if (!cls.isAnnotationPresent(Mixer.class))
            throw new IllegalArgumentException("Mixins must be annotated with @Mixer");
        final String type = cls.getAnnotation(Mixer.class).value();

        this.validateMixin(type);
        Set<MixinMetaData> mixins = new HashSet<>();
        for (Method method : cls.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Injection.class)) {
                final Injection injection = method.getAnnotation(Injection.class);
                final String methodTo = this.mixinToMethodName(method);

                if (!this.declaredMethod(type, methodTo, method.getParameterTypes()))
                    throw new IllegalArgumentException("Failed to find a appropriate method to mix to");

                mixins.add(new MixinMetaData(cls.getName(),
                        this.byteCodeSignature(method),
                        type,
                        this.byteCodeSignature(method, methodTo), injection.type(), injection.priority()));
            }
        }
        return mixins;
    }

    private String byteCodeSignature(Method method, String name) {
        final Class<?> methodReturn = method.getReturnType();

        final StringBuilder builder = new StringBuilder(name);

        builder.append('(');
        for (Class<?> type : method.getParameterTypes()) {
            builder.append(type.isPrimitive() ?
                    ByteCodeUtils.primitiveType(type) :
                    type.isArray() ?
                            type.getName() :
                            "L" + type.getName().replace('.', '/') + ";");
        }
        builder.append(')');

        builder.append(methodReturn.isPrimitive() ?
                ByteCodeUtils.primitiveType(methodReturn) :
                methodReturn.getName());

        return builder.toString();
    }

    private String byteCodeSignature(Method method) {
       return this.byteCodeSignature(method, method.getName());
    }



    private String mixinToMethodName(Method method) {
        if (!method.isAnnotationPresent(Injection.class)) return method.getName();
        final Injection injection = method.getAnnotation(Injection.class);
        return injection.to().equals(Injection.METHOD_SELF) ? method.getName() : injection.to();
    }

    private boolean declaredMethod(String cls, String method, Class<?>... parameterTypes) {
        //TODO Might wanna replace with ASM
        return true;
    }

    public void dumpAll() {
        this.libRegistry.registerAll();
        this.mixinRegistry.registerAll();
    }

    public final Class<?> retrieveClass(String cls) throws ClassNotFoundException {
        return ContextPoolManager.loadClass(cls);
    }

//    private CompletableFuture<Pointer> registerMixin(MixinMetaData)


//    public ClassLoader retrieveLoader(UUID)
}
