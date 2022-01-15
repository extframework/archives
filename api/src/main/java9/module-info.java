module yakclient.bmu.api {
    requires transitive org.objectweb.asm;
    requires transitive org.objectweb.asm.tree;
    requires kotlin.stdlib;
    requires kotlin.reflect;
    requires java.instrument;
    requires transitive org.objectweb.asm.util;
    requires net.bytebuddy.agent;
    requires jdk.attach;

    exports net.yakclient.bmu.api;
    exports net.yakclient.bmu.api.extension;
}