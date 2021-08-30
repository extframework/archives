package net.yakclient.mixins.base

abstract class DelegatingTransformer<T>(
    private val delegate: InjectionTransformer<T>
) : InjectionTransformer<T> {
    override fun invoke(c: T): T = delegate(c)
}