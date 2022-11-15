package net.yakclient.archives.mixin.test

import net.yakclient.archives.Archives
import net.yakclient.archives.transform.TransformerConfig
import org.objectweb.asm.ClassReader

fun Class<*>.transform(config: TransformerConfig): Class<*> {
    val bytes = Archives.resolve(ClassReader(name), config)

    val loader = object : ClassLoader() {
        fun defineClass(name: String, bytes: ByteArray) = super.defineClass(name, bytes, 0, bytes.size)
    }

    val c = loader.defineClass(name, bytes)

    return c
}

fun Class<*>.noArgInstance(): Any = this.getConstructor().newInstance()