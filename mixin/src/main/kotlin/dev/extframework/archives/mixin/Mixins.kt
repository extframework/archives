package dev.extframework.archives.mixin

import java.io.InputStream

public object Mixins {
    public interface ClassDataProvider {
        public val classname: String

        public fun getClass() : InputStream
    }

    public fun ClassDataProvider(clazz: Class<*>) : ClassDataProvider = object : ClassDataProvider {
        override val classname: String = clazz.name

        override fun getClass(): InputStream = checkNotNull(clazz.classLoader.getResourceAsStream(classname.replace('.', '/') + ".class"))
    }
}