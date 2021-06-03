package net.yakclient.mixin.base.registry.pool

import net.yakclient.mixin.base.internal.bytecode.BytecodeMethodModifier
import net.yakclient.mixin.base.internal.bytecode.MixinDestination
import net.yakclient.mixin.base.internal.bytecode.MixinSource
import net.yakclient.mixin.base.internal.bytecode.MixinSource.MixinProxySource
import net.yakclient.mixin.base.internal.instruction.Instruction
import net.yakclient.mixin.base.target.ClassTarget
import net.yakclient.mixin.base.target.ClassTarget.Companion.of
import net.yakclient.mixin.base.internal.loader.ContextPoolManager.defineClass
import net.yakclient.mixin.base.target.Target
import net.yakclient.mixin.base.registry.FunctionalProxy
import net.yakclient.mixin.base.registry.pool.RegistryPool.PoolQueue.PoolNode
import net.yakclient.mixin.base.registry.pool.RegistryPool.PoolQueue.ProxiedPoolNode
import net.yakclient.mixin.base.registry.proxy.MixinProxyManager
import java.io.IOException

class MixinRegistryPool : RegistryPool<MixinMetaData>() {
    /*
        The pool holds locations to where mixins will be injected. These will be and only be MethodLocations(No child).
        The PoolQueue
     */
    private val methodModifier: BytecodeMethodModifier = BytecodeMethodModifier()

    @Throws(ClassNotFoundException::class)
    override fun pool(type: MixinMetaData): Location {
        val key: ClassLocation = ClassLocation.Companion.fromDataDest(type)
        if (!pool.containsKey(key)) pool[key] = PoolQueue()
        pool[key]!!.add(type)
        return key
    }

    /*
        Using the PointerManager#register() is a sub-optimal solution. This will(unfortunately) work. What is happening
        is that i am registering a null classloader to register a place keeper. Then overloading it once we have to
        complete our future.
     */
    @Throws(ClassNotFoundException::class)
    fun pool(type: MixinMetaData, proxy: FunctionalProxy): Location {
        val key: ClassLocation = ClassLocation.fromDataDest(type)
        if (!pool.containsKey(key)) pool[key] = PoolQueue()
        val rp = MixinProxyManager.registerProxy(proxy)
        pool[key]!!.add(type, rp)
        return key
    }

    /*
        The registry combiner will take a QualifiedMethodLocation as the source and then a MethodLocation as the destination.
        It wont take a collection because in the end that doest make sense and makes it way more complicated
     */
    override fun register(location: Location): Target {
        return try {
            val pool: PoolQueue<MixinMetaData> = pool[location]!!

            require(location is ClassLocation) { "Provided location must be a ClassLocation!" }
            val target: ClassTarget = of(location.cls)

            val destinations = HashMap<String, MutableSet<MixinSource>>()

            for (node in pool.queue) {
                val method = node.value.methodTo
                destinations.putIfAbsent(method, HashSet())
                destinations[method]!!.add(getSource(node))
            }
            val mixins : Array<MixinDestination> = destinations.map { (k: String, v: MutableSet<MixinSource>) -> MixinDestination(k, v) }.toTypedArray()

            val b = methodModifier.combine<Instruction>(location.cls, *mixins)
            defineClass(target, location.cls, b)
            target
        } catch (e: IOException) {
            throw IllegalArgumentException("Given class has failed ASM reading. Ex: " + e.message)
        }
    }

    private fun getSource(node: PoolNode<MixinMetaData>): MixinSource {
        return if (node is ProxiedPoolNode<MixinMetaData>) MixinProxySource(
            QualifiedMethodLocation.fromDataOrigin(node.value),
            node.proxy
        )
        else MixinSource(QualifiedMethodLocation.fromDataOrigin(node.value))
    }
}