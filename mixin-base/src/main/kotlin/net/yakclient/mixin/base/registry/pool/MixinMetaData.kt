package net.yakclient.mixin.base.registry.pool

data class MixinMetaData(
    val classFrom: String,
    val methodFrom: String,
    val classTo: String,
    val methodTo: String,
    val type: Int,
    val priority: Int
)
