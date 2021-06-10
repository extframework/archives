module mixins.base {
    exports net.yakclient.mixin.base.registry;
    exports net.yakclient.mixin.base.registry.pool;
    exports net.yakclient.mixin.base.registry.proxy;

    requires transitive mixins.api;

    requires org.objectweb.asm;
    requires org.objectweb.asm.tree;
    requires org.jetbrains.annotations;
    requires kotlin.stdlib;
}