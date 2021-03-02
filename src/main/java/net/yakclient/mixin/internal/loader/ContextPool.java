package net.yakclient.mixin.internal.loader;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public abstract class ContextPool {
    private final Map<PackageTarget, Context> targets;

    private final ClassLoader loader;

    public ContextPool() {
        this.targets = new HashMap<>();
        this.loader = Thread.currentThread().getContextClassLoader();
    }

//    public Context addTarget(String path, ClassLoader loader) {
//        final PackageTarget target = this.createTarget(path);
//        return this.targets.put(target, this.createContext(loader, target));
//    }

    private boolean encapsulatedTargetExists(PackageTarget target) {
        for (PackageTarget packageTarget : this.targets.keySet()) {
            if (packageTarget.isTargetOf(target)) return true;
        }
        return false;
    }

    public PackageTarget encapsulatedTarget(PackageTarget target) {
        if (!this.encapsulatedTargetExists(target)) throw new IllegalStateException("Target MUST be encapsulated!");
        for (PackageTarget packageTarget : this.targets.keySet()) {
            if (packageTarget.isTargetOf(target)) return packageTarget;
        }
        throw new IllegalStateException("Target MUST be encapsulated!");
    }

    public Context addTarget(PackageTarget target, ClassLoader loader) {
        return this.targets.put(this.getTarget(target), this.createContext(loader, target));
    }

    public Context addTarget(PackageTarget target) {
        return this.targets.putIfAbsent(this.getTarget(target), this.createContext(null, target));
    }

    private PackageTarget getTarget(PackageTarget target) {
        return this.encapsulatedTargetExists(target) ? this.encapsulatedTarget(target) : target;
    }

    public boolean isTargeted(String target) {
        final PackageTarget packageTarget = this.createTarget(target);
        for (final PackageTarget pT : this.targets.keySet()) {
            if (pT.isTargetOf(packageTarget)) return true;
        }
        return false;
    }

    public boolean isTargeted(PackageTarget target) {
        for (final PackageTarget pT : this.targets.keySet()) {
            if (pT.isTargetOf(target)) return true;
        }
        return false;
    }

    @Nullable
    Class<?> loadClassOrNull(String name) {
        final PackageTarget target = this.createTarget(name);
        if (this.targets.containsKey(target)) return this.targets.get(target).findClass(name);
        else if (this.encapsulatedTargetExists(target)) return this.targets.get(this.encapsulatedTarget(target)).findClass(name);
        return null;
    }

    private Context createContext(ClassLoader loader, PackageTarget target) {
        return new Context(loader, target, this);
    }

    PackageTarget createTarget(String path) {
        try {
            if (this.loader.loadClass(path) != null) return ClassTarget.create(path);
            return PackageTarget.create(path);
        } catch (ClassNotFoundException e) {
            return PackageTarget.create(path);
        }
    }
}