package net.yakclient.mixins.base.internal.bytecode

class MixinDestination(
    val method: String,
    val sources:Set<MixinSource>
)
//    class MixinDestBuilder(val method: String) {
//        private val sources: MutableSet<MixinSource>
//        init {
//            sources = HashSet()
//        }
//
//        fun addSource(source: MixinSource): MixinDestBuilder {
//            sources.add(source)
//            return this
//        }
//
//        fun build(): MixinDestination {
//            return MixinDestination(this.method, this.sources.toTypedArray())
//        }
//    }
