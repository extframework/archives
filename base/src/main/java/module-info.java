module yakclient.mixins.base {
    exports net.yakclient.mixins.base.registry;
    exports net.yakclient.mixins.base.registry.pool;
    exports net.yakclient.mixins.base.registry.proxy;
    exports net.yakclient.mixins.base.extension;

    requires transitive yakclient.mixins.api;
    requires yakclient.mixins.base.agent;

    requires org.objectweb.asm;
    requires org.objectweb.asm.tree;
    requires org.jetbrains.annotations;
    requires kotlin.stdlib;
    requires kotlin.reflect;
    requires kotlinx.coroutines.core.jvm;
    requires java.instrument;
    requires org.objectweb.asm.util;
//    requires reflections;
}