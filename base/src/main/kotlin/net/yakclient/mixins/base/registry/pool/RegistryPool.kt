package net.yakclient.mixins.base.registry.pool

import java.util.*

abstract class RegistryPool<T> {
    protected val pool: MutableMap<Location, PoolQueue<T>>
    init {
        pool = HashMap()
    }

    @Throws(ClassNotFoundException::class)
   abstract fun pool(type: T): Location

    abstract fun mix(location: Location)

    fun mixAll(location: Location) {
        if (pool.containsKey(location)) mix(location)
    }

    fun mixAll() {
        for (location in pool.keys) {
            mix(location)
        }
    }

    class PoolQueue<T> {
        val queue: Queue<PoolNode<T>>
        init {
            queue = LinkedList()
        }

        fun add(type: T): PoolQueue<T> {
            queue.add(PoolNode(type))
            return this
        }

        fun add(type: T, proxy: UUID): PoolQueue<T> {
            queue.add(ProxiedPoolNode(type, proxy))
            return this
        }

        open class PoolNode<T> internal constructor(val value: T)
        class ProxiedPoolNode<T> internal constructor(value: T, val proxy: UUID) : PoolNode<T>(value)
    }
}