package dev.extframework.archives.mixin.test

import dev.extframework.archives.Archives
import dev.extframework.archives.transform.TransformerConfig
import org.objectweb.asm.ClassReader

fun Class<*>.transform(config: TransformerConfig): Class<*> {
    val bytes = Archives.applyConfig(ClassReader(name), config)

    val loader = object : ClassLoader() {
        fun defineClass(name: String, bytes: ByteArray) = super.defineClass(name, bytes, 0, bytes.size)
    }

    val c = loader.defineClass(name, bytes)

    return c
}

fun Class<*>.noArgInstance(): Any = this.getConstructor().apply {trySetAccessible()}.newInstance()