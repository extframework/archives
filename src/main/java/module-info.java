module yakclient.archives {
    requires transitive org.objectweb.asm;
    requires transitive org.objectweb.asm.tree;
    requires kotlin.stdlib;
    requires kotlin.reflect;
    requires java.instrument;
    requires transitive org.objectweb.asm.util;
    requires jdk.attach;
    requires yakclient.common.util;

    exports net.yakclient.archives.api;
    exports net.yakclient.archives.api.extension;
    exports net.yakclient.archives.api.transform;
}