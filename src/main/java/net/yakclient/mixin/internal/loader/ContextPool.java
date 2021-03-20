package net.yakclient.mixin.internal.loader;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public abstract class ContextPool {
    private final Map<PackageTarget, Context> targets;

    private final ClassLoader loader;

    public ContextPool() {
        this.targets = new HashMap<>();
        this.loader = ClassLoader.getSystemClassLoader();
    }

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
        final PackageTarget realTarget = this.getTarget(target);
        return this.targets.put(realTarget, this.createContext(loader, realTarget));
    }

    public Context addTarget(PackageTarget target) {
        final PackageTarget realTarget = this.getTarget(target);
        final ClassLoader loader = this.targets.containsKey(realTarget) ? this.targets.get(realTarget).getLoader() : ContextPoolManager.createLoader(realTarget);
        return this.targets.putIfAbsent(this.getTarget(target), this.createContext(loader, target));
    }

    @Contract(pure = true)
    public PackageTarget getTarget(PackageTarget target) {
        return this.encapsulatedTargetExists(target) ? this.encapsulatedTarget(target) : target;
    }

    public boolean isTargeted(String target) {
        final PackageTarget packageTarget = this.createTarget(target);
        return this.isTargeted(packageTarget);
    }

    public boolean isTargeted(PackageTarget target) {
        for (final PackageTarget pT : this.targets.keySet()) {
            if (pT.isTargetOf(target)) return true;
        }
        return false;
    }

    @Nullable
    public Class<?> loadClassOrNull(String name) {
        final PackageTarget target = this.getTarget(this.createTarget(name));
        return this.targets.get(target).findClass(name);
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

    public Class<?> defineClass(ClassTarget target, byte[] bytes) {
        final PackageTarget finalTarget = this.getTarget(target);
        final Context context = this.targets.get(finalTarget);
        final String name = target.toString();

        if (!context.getLoader().isDefined(name)) return context.getLoader().defineClass(name, bytes);
        else this.targets.put(finalTarget, this.createContext(ContextPoolManager.createLoader(finalTarget), finalTarget));

        return this.targets.get(finalTarget).getLoader().defineClass(name, bytes);
    }
}