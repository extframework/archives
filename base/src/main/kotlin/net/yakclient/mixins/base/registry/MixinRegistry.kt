package net.yakclient.mixins.base.registry

import net.yakclient.mixins.api.Injection
import net.yakclient.mixins.api.METHOD_SELF
import net.yakclient.mixins.api.Mixer
import net.yakclient.mixins.api.RESOLVE_MODULE
import net.yakclient.mixins.base.internal.bytecode.ByteCodeUtils.primitiveType
import net.yakclient.mixins.base.registry.pool.ExternalLibRegistryPool
import net.yakclient.mixins.base.registry.pool.MixinMetaData
import net.yakclient.mixins.base.registry.pool.MixinRegistryPool
import java.lang.reflect.Method
import java.net.URL
import net.yakclient.mixins.base.internal.loader.ContextPoolManager
import net.yakclient.mixins.base.registry.proxy.FunctionalProxy
import net.yakclient.mixins.base.target.JarTarget
import net.yakclient.mixins.base.target.ModuleTarget

class MixinRegistry {
    private val libRegistry: ExternalLibRegistryPool = ExternalLibRegistryPool()
    private val mixinRegistry: MixinRegistryPool = MixinRegistryPool()

    //TODO there are alot of issues with class reloading for example if a mixin class gets loaded(which it by definition has to) then it loads all of the classes it might reference in parameters etc. so we could fix this by not allowing the loading of mixin classes? that might be an option.
    /**
     * Registers a Mixin from the given class.
     *
     *
     * Right now it doest make sense to return a pointer after adding the mixin.
     * there would be virtually nothing you would need to do with it and accessing
     * the class would already be provided by calling the default classloader etc.
     *
     * @param cls The class to take mixins from
     * @return MixinRegistry for easy access.
     */
    fun registerMixin(cls: Class<*>): ContextHandle {
        val data = data(cls)
        for (datum in data) mixinRegistry.pool(datum)
        return ModuleContextHandle(moduleTarget(cls))
    }

    fun registerMixin(cls: Class<*>, proxy: FunctionalProxy): ContextHandle {
        val data = data(cls)
        for (datum in data) mixinRegistry.pool(datum, proxy)
        return ModuleContextHandle(moduleTarget(cls))
    }

    fun registerLib(url: URL): ContextHandle {
        this.libRegistry.pool(url)
        return JarContextHandle(JarTarget(url))
    }

    private fun moduleTarget(cls: Class<*>): ModuleTarget {
        require(cls.isAnnotationPresent(Mixer::class.java)) { "Mixins must be annotated with @Mixer" }
        return resolveModule(cls.getAnnotation(Mixer::class.java))
    }

    private fun resolveModule(it: Mixer) = when (it.module) { //TODO Take out method and replace with inline
        RESOLVE_MODULE -> requireNotNull(ContextPoolManager.resolveModuleByClass(it.value)) { "Failed to find module by class: ${it.value}" }
        else -> requireNotNull(ContextPoolManager.resolveModuleByModule(it.module)) { "Failed to find target for module by name: ${it.module}" }
    }

    private fun data(cls: Class<*>): Set<MixinMetaData> {
        require(cls.isAnnotationPresent(Mixer::class.java)) { "Mixins must be annotated with @Mixer" }
        val mixer = cls.getAnnotation(Mixer::class.java)
        val mixins = HashSet<MixinMetaData>()
        for (method in cls.declaredMethods) {
            if (method.isAnnotationPresent(Injection::class.java)) {
                val injection = method.getAnnotation(Injection::class.java)
                val methodTo = mixinToMethodName(method)
                require(
                    declaredMethod(
                        mixer.value,
                        methodTo,
                        *method.parameterTypes
                    )
                ) { "Failed to find a appropriate method to mix to" }
                mixins += MixinMetaData(
                    cls.name,
                    byteCodeSignature(method),
                    mixer.value,
                    if (injection.to == METHOD_SELF) byteCodeSignature(method) else methodTo,
                    injection.type,
                    injection.priority
                )
            }
        }
        return mixins
    }

    private fun byteCodeSignature(method: Method, name: String = method.name): String {
        val methodReturn = method.returnType
        val builder = StringBuilder(name)
        builder.append('(')
        for (type in method.parameterTypes) {
            builder.append(
                when {
                    type.isPrimitive -> primitiveType(type)
                    type.isArray -> type.name
                    else -> "L" + type.name.replace(
                        '.',
                        '/'
                    ) + ";"
                }
            )
        }
        builder.append(')')
        builder.append(if (methodReturn.isPrimitive) primitiveType(methodReturn) else methodReturn.name)
        return builder.toString()
    }

    private fun mixinToMethodName(method: Method): String {
        if (!method.isAnnotationPresent(Injection::class.java)) return method.name
        val injection = method.getAnnotation(Injection::class.java)
        return if (injection.to == METHOD_SELF) method.name else injection.to
    }

    private fun declaredMethod(cls: String, method: String, vararg parameterTypes: Class<*>): Boolean {
        //TODO Might wanna replace with ASM
        return true
    }

    fun mixAll() {
        libRegistry.mixAll()
        mixinRegistry.mixAll()
    }

    fun mixLibs() {
        libRegistry.mixAll()
    }

    fun mixMixins() {
        mixinRegistry.mixAll()
    }
}