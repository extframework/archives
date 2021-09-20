module yakclient.mixins.base {
    requires transitive yakclient.mixins.api;

    requires org.objectweb.asm;
    requires org.objectweb.asm.tree;
    requires kotlin.stdlib;
    requires kotlin.reflect;
    requires java.instrument;
    requires org.objectweb.asm.util;
    requires net.bytebuddy.agent;
    requires jdk.attach;
//    exports n
    exports net.yakclient.mixins.base;
    exports net.yakclient.mixins.base.extension;
}