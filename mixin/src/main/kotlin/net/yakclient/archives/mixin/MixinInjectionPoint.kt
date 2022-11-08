package net.yakclient.archives.mixin

public fun interface MixinInjectionPoint  {
    public fun apply(context: MixinInjectionContext) : List<MixinInjector>
}