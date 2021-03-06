//package net.yakclient.mixin.registry.pool;
//
//import net.yakclient.mixin.registry.MixinMetaData;
//
//import java.util.UUID;
//
//public class ProxiedMethodLocation extends MethodLocation {
//    private final UUID proxy;
//
//    public ProxiedMethodLocation(Class<?> cls, String method,  UUID proxy) {
//        super(cls, method);
//        this.proxy = proxy;
//    }
//
//    public static ProxiedMethodLocation fromDataDest(MixinMetaData data, UUID proxy) {
//        return new ProxiedMethodLocation(data.getClassTo(), data.getMethodTo(), proxy);
//    }
//
//    public static ProxiedMethodLocation fromDataOrigin(MixinMetaData data, UUID proxy) {
//        return new ProxiedMethodLocation(data.getClassFrom(), data.getMethodFrom(), proxy);
//    }
//
//    public UUID getProxy() {
//        return proxy;
//    }
//}
