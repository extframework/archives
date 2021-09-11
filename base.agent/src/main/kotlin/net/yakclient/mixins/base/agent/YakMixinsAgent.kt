@file:JvmName("YakMixinsAgentKt")

package net.yakclient.mixins.base.agent

import java.lang.instrument.Instrumentation
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object YakMixinsAgent {
    lateinit var instrumentation: Instrumentation// by object : ReadWriteProperty<YakMixinsAgent, Instrumentation?> {
//        override fun getValue(thisRef: YakMixinsAgent, property: KProperty<*>): Instrumentation? = instrumentation
//
//        override fun setValue(thisRef: YakMixinsAgent, property: KProperty<*>, value: Instrumentation?) =
//            if (instrumentation == null)
//                instrumentation = value
//            else throw IllegalStateException("Cannot re-set existing instrumentation!")
//    }
}

fun agentmain(args: String?, instrumentation: Instrumentation) {
    YakMixinsAgent.instrumentation = instrumentation
}


