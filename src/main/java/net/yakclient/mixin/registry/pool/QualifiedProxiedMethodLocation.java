//package net.yakclient.mixin.registry.pool;
//
//import net.yakclient.mixin.api.InjectionType;
//import net.yakclient.mixin.registry.MixinMetaData;
//
//import java.util.Objects;
//import java.util.UUID;
//
//public class QualifiedProxiedMethodLocation extends QualifiedMethodLocation {
//    private final UUID proxy;
//
//    public QualifiedProxiedMethodLocation(Class<?> cls, String method, InjectionType injectionType, int priority, UUID proxy) {
//        super(cls, method, injectionType, priority);
//        this.proxy = proxy;
//    }
//
//    public static QualifiedProxiedMethodLocation fromDataDest(MixinMetaData data, UUID proxy) {
//        return new QualifiedProxiedMethodLocation(data.getClassTo(), data.getMethodTo(), data.getType(), data.getPriority(), proxy);
//    }
//
//    public static QualifiedProxiedMethodLocation fromDataOrigin(MixinMetaData data, UUID proxy) {
//        return new QualifiedProxiedMethodLocation(data.getClassFrom(), data.getMethodFrom(), data.getType(), data.getPriority(), proxy);
//    }
//
//    public UUID getProxy() {
//        return proxy;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        if (!super.equals(o)) return false;
//        QualifiedProxiedMethodLocation that = (QualifiedProxiedMethodLocation) o;
//        return Objects.equals(proxy, that.proxy);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(super.hashCode(), proxy);
//    }
//}
