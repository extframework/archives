module yakclient.mixins.base {
    requires transitive yakclient.mixins.api;
    requires yakclient.mixins.base.agent;

    requires org.objectweb.asm;
    requires org.objectweb.asm.tree;
    requires kotlin.stdlib;
    requires kotlin.reflect;
    requires java.instrument;
    requires org.objectweb.asm.util;
//    requires kotlinx.coroutines.core.jvm;

    exports net.yakclient.mixins.base;
    exports net.yakclient.mixins.base.extension;
}