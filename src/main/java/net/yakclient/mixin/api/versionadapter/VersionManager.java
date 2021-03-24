package net.yakclient.mixin.api.versionadapter;

import net.yakclient.mixin.internal.bytecode.ByteCodeUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VersionManager {
    private final Map<String, VersionState> states;
    private static VersionManager instance;

    private static final String BYTECODE_METHOD_SYNTAX = "^((?:[a-zA-Z0-9$_])+)" +
            "\\(((?:[ZCBSIFJD]|(?:L[a-zA-Z0-9$_.]+;)|(?:\\[[ZCBSIFJD])|(?:\\[L[a-zA-Z0-9$_.]+;))*)\\)" +
            "(?:[ZCBSIFJDV]|(?:L.+;)|(?:\\[[ZCBSIFJD]|(?:\\[L.+;)))";

    public VersionManager() {
        this.states = new HashMap<>();
    }

    private static synchronized VersionManager getInstance() {
        if (instance == null) instance = new VersionManager();
        return instance;
    }

    public static void apply(String target, String version) {
        initState(version, target);
    }

    public static void apply(VersionMapping mapping, String version) {
        initState(version, mapping.getTarget()).getMapping().collate(mapping);
    }

    public static boolean isApplied(String target) {
        return getInstance().states.containsKey(target);
    }

    public static String version(String target) {
        return getInstance().states.get(target).getVersion();
    }

    public static ClassVersion classVersion(String target, String key) {
        return getInstance().states.get(target).retrieveName(key);
    }

    public static Class<?> clazz(String target, String key) throws ClassNotFoundException {
        return ClassLoader.getSystemClassLoader().loadClass(classVersion(target, key).getClazz());
    }

    public static Method method(String target, String key, String methodIdentifier) {
        final ClassVersion clazz = classVersion(target, key);

        try {
            final Class<?> methodCls = ClassLoader.getSystemClassLoader().loadClass(clazz.getClazz());
            return getInstance().parseToMethod(methodCls, clazz.getMethod(methodIdentifier));
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Failed to find targeted method: " + clazz.getMethod(methodIdentifier) + " in class: " + clazz.getClazz());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Failed to find class: " + clazz.getClazz());
        }
    }

    private Method parseToMethod(Class<?> root, String signature) throws NoSuchMethodException, ClassNotFoundException {
        Matcher m = Pattern.compile(BYTECODE_METHOD_SYNTAX).matcher(signature);
        if (!m.find())
            throw new IllegalArgumentException("Given signature: " + signature + " is invalid, cannot load.");
        final String name = m.group(1);
        final Class<?>[] params = this.descriptorToClasses(m.group(2), root.getClassLoader());
        return root.getDeclaredMethod(name, params);
    }

    private Class<?>[] descriptorToClasses(String descriptor, ClassLoader loader) throws ClassNotFoundException {
        final List<Class<?>> params = new ArrayList<>();
        boolean objectMatching = false;
        boolean arrayMatching = false;
        StringBuilder builder = new StringBuilder();

        for (byte aByte : descriptor.getBytes()) {
            //First just check if the byte is a primitive, if it is
            //and we arent trying to match a array/object then we can
            //confirm it is a valid Object and load it.
            final char c = (char) aByte;
            if (ByteCodeUtils.isPrimitiveType(c) && !objectMatching && !arrayMatching)
                params.add(ByteCodeUtils.primitiveType(c));
                //If its not a primitive we can check to see if its a start
                //of a array, if it is then we dont assume matching for an
                //object but we still append the byte.
            else if (aByte == '[') {
                arrayMatching = true;
                builder.append(c);
                //Next if it is a array, the builder size is 1 and the byte
                //is 'L' then we know it must be the start of a object array.
            } else if (aByte == 'L' && arrayMatching && builder.length() == 1) {
                objectMatching = true;
                builder.append(c);
                //If its not a Object array it could be a primitive one. We
                //check if the builder length is one, the byte is a primitive
                //identifier and that we are trying to match for a array. We can
                //now safely add this class and load it with the default classloader.
            } else if (ByteCodeUtils.isPrimitiveType(c) && arrayMatching && builder.length() == 1) {
                params.add(Class.forName("[" + aByte));
                arrayMatching = false;
                //We can check for the end of a object array by seeing
                //if the current byte is ; and that we are matching
                //for arrays and objects.
            } else if (aByte == ';' && arrayMatching && objectMatching) {
                params.add(Class.forName(builder.toString() + ";", true, loader));
                builder.delete(0, builder.length());
                objectMatching = false;
                arrayMatching = false;
                //Now that we know we arent matching for a array we can
                //start matching for Objects, if it the current byte is
                //L and we arent Object/array matching then we start.
            } else if (aByte == 'L' && !objectMatching && !arrayMatching) objectMatching = true;
                //If we are object matching and we see an end
                //we know this must be the object and add it.
            else if (aByte == ';' && objectMatching) {
                params.add(loader.loadClass(builder.toString()));
                builder.delete(0, builder.length());
                objectMatching = false;
                //This applies to Arrays and Objects, if we are
                //matching for an object we can append it.
            } else if (objectMatching) builder.append(c);
        }
        return params.toArray(new Class[0]);
    }

    public static Field field(String target, String key, String fieldIdentifier) {
        final ClassVersion clazz = classVersion(target, key);

        try {
            final Class<?> fieldCls = ClassLoader.getSystemClassLoader().loadClass(clazz.getClazz());
            return fieldCls.getDeclaredField(clazz.getField(fieldIdentifier));
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Failed to find class: " + clazz.getClazz());
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException("Failed to fine method: " + clazz.getField(fieldIdentifier) + " in class: " + clazz.getClazz());
        }
    }
    private static VersionState initState(String version, String target) {
        final VersionState value = new VersionState(version, target);
        getInstance().states.put(target, value);
        return value;
    }

    private static class VersionState {
        private final String version;
        private final CollatedVMapping mapping;

        public VersionState(String version, String target) {
            this.version = version;
            this.mapping = new CollatedVMapping(target);
        }

        public String getVersion() {
            return version;
        }

        public CollatedVMapping getMapping() {
            return mapping;
        }

        public ClassVersion retrieveName(String key) {
            return this.mapping.retrieve(key, this.version);
        }
    }
}
