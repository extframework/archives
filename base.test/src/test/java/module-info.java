open module yakclient.mixins.base.test.test {
    requires kotlin.stdlib;
    requires org.junit.jupiter.api;
    requires yakclient.mixins.base;
    requires org.objectweb.asm.tree;
    requires jdk.unsupported;

    requires net.bytebuddy.agent;
    requires yakclient.mixins.base.agent;
    requires java.instrument;
    requires jdk.attach;

//    exports net.yakclient.mixins.base.test.extension;
}