module yakclient.archives.api {
    requires transitive org.objectweb.asm;
    requires transitive org.objectweb.asm.tree;
    requires kotlin.stdlib;
    requires kotlin.reflect;
    requires java.instrument;
    requires transitive org.objectweb.asm.util;
    requires jdk.attach;
    requires yakclient.common.util;

    exports net.yakclient.bmu.api;
    exports net.yakclient.bmu.api.extension;
    exports net.yakclient.bmu.api.transform;
}