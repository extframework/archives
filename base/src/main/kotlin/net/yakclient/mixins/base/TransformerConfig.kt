package net.yakclient.mixins.base

class TransformerConfig(
    val classTransformers: List<ClassTransformer>,
    val methodTransformers: List<MethodTransformer>,
    val fieldTransformers: List<FieldTransformer>
) {
    companion object {
        fun of(block: TransformerConfigScope.() -> Unit): TransformerConfig =
            TransformerConfigScope().also(block)
                .let { TransformerConfig(it.classTransformers, it.methodTransformers, it.fieldTransformers) }

    }

    class TransformerConfigScope(
        val classTransformers: MutableList<ClassTransformer> = ArrayList(),
        val methodTransformers: MutableList<MethodTransformer> = ArrayList(),
        val fieldTransformers: MutableList<FieldTransformer> = ArrayList()
    ) {
        fun addCt(t: ClassTransformer) = classTransformers.add(t)
        fun addMt(t: MethodTransformer) = methodTransformers.add(t)
        fun addFt(t: FieldTransformer) = fieldTransformers.add(t)
    }
}