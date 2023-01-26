package net.yakclient.archives.mixin

import net.yakclient.archives.transform.TransformerConfig

public interface MixinInjection<T: MixinInjection.InjectionData> {
    public fun apply(data: T) : TransformerConfig.Mutable

    public interface InjectionData
}