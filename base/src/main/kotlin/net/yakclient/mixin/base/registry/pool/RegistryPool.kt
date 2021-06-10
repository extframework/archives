package net.yakclient.mixin.base.registry.pool

import java.util.*

abstract class RegistryPool<T> {
    protected val pool: MutableMap<Location, PoolQueue<T>>
    init {
        pool = HashMap()
    }

    @Throws(ClassNotFoundException::class)
    abstract fun pool(type: T): Location

    abstract fun register(location: Location)

    fun registerAll(location: Location) {
        if (pool.containsKey(location)) register(location)
    }

    fun registerAll() {
        for (location in pool.keys) {
            register(location)
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