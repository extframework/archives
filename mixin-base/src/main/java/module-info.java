module mixin.base {
    exports net.yakclient.mixin.base.registry;
    exports net.yakclient.mixin.base.registry.pool;

    requires mixin.api;
    requires org.objectweb.asm;
    requires org.objectweb.asm.tree;
    requires org.jetbrains.annotations;
}