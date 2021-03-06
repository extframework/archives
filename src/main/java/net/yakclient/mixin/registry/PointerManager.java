//package net.yakclient.mixin.registry;
//
//import net.yakclient.mixin.internal.loader.PackageTarget;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.*;
//
//public class PointerManager {
//    private final Set<PackageTarget> loaders;
//    private static PointerManager instance;
//
//    private PointerManager() {
//        this.loaders = new HashSet<>();
//    }
//
//    private static synchronized PointerManager getInstance() {
//        if (instance == null) instance = new PointerManager();
//        return instance;
//    }
//
//    public static @NotNull UUID register(PackageTarget target) {
//        final UUID key = UUID.randomUUID();
//        getInstance().loaders.add(target);
//        return key;
//    }
//
//    @Deprecated
//    public static @NotNull UUID register() {
////        final UUID key = UUID.randomUUID();
////        getInstance().loaders.put(key, null);
//        return null;
//    }
//
//    public static PackageTarget overload(PackageTarget target) {
//        getInstance().loaders.add(target);
//        return target;
//    }
//
////    @Nullable
////    public static PackageTarget retrieve(UUID uuid) {
////        return getInstance().loaders.get(uuid);
////    }
//
////    public static boolean hasPointer(UUID uuid) {
////        return getInstance().loaders.containsKey(uuid);
////    }
//}
