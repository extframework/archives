package net.yakclient.mixin.internal.loader;

public class ProxyClassLoader extends ClassLoader {
    private static final String CLASS_MANAGER = "net.yakclient.mixin.internal.loader.ClassManager";

    public ProxyClassLoader(ClassLoader parent) {
        super(parent);
        Thread.currentThread().setContextClassLoader(this);
    }

    public Class<?> defineClass(byte[] b, String name) {
        return super.defineClass(name, b, 0, b.length);
    }


    public boolean isDefined(String cls) {
        return super.findLoadedClass(cls) != null;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
//        if (super.findLoadedClass(CLASS_MANAGER) == null && !CLASS_MANAGER.equals(name)) this.loadClass(CLASS_MANAGER, true);
//        if (this.findLoadedClass(CLASS_MANAGER) != null || ClassManager.hasOverload(name))
//            return ClassManager.retrieve(name);

        if (ContextPoolManager.isTargeted(name)) return ContextPoolManager.loadClass(name);
        return super.loadClass(name);

//
//        try {
//            Class<?> c = this.findLoadedClass(name);
//            if (c == null) {
//
//                if (name.toLowerCase().startsWith("java.")) return super.loadClass(name, resolve);
//                System.out.println(name);
//
//                final InputStream classData = getResourceAsStream(name.replace('.', '/') + ".class");
//                if (classData == null) throw new ClassNotFoundException("Failed to find class " + name);
//
//                final int available = classData.available();
//                final byte[] bytes = new byte[available];
//                final int read = classData.read(bytes);
//                System.out.println("THE AVAILABLE WAS: " + available);
//                System.out.println("THE READ WAS: " + read);
//                System.out.println("THE LENGTH IS: " + bytes.length);
////                if (read != bytes.length) System.out.println("It failed, heres why");
////                    throw new ClassNotFoundException("Failed to read the total size of the input stream");
//
//                System.out.println(Arrays.toString(bytes));
//
//                final byte[] sized = new byte[read];
//                System.arraycopy(bytes, 0, sized, 0, read);
//                System.out.println(Arrays.toString(sized));
////                System.out.println("New array length is" + sized.length);
//                System.out.println("--------------------------");
//                if (name.equals("org.objectweb.asm.ClassWriter")) Thread.sleep(100000);
//
//                c = this.defineClass(name, sized, 0, read);
//            }
//            if (resolve) this.resolveClass(c);
//
//            return c;
//        } catch (IOException | InterruptedException e) {
//            throw new ClassNotFoundException(e.getMessage());
//        }
    }

}
