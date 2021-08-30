package net.yakclient.mixins.base

class TransformerConfig(
    val classTransformers: List<ClassTransformer>,
    val methodTransformers: List<MethodTransformer>,
    val fieldTransformers: List<FieldTransformer>
)