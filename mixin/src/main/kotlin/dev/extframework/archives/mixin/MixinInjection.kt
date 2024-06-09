package dev.extframework.archives.mixin

import dev.extframework.archives.transform.TransformerConfig

public interface MixinInjection<T: MixinInjection.InjectionData> {
    public fun apply(data: T) : TransformerConfig.Mutable

    public interface InjectionData
}