package net.yakclient;

import net.yakclient.mixin.internal.loader.ProxyClassLoader;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

public class TestingProxyCSL extends BlockJUnit4ClassRunner {
    private static ClassLoader csl;

    public TestingProxyCSL(Class<?> testClass) throws InitializationError {
        super(loadFromCustomClassloader(testClass));
    }

    private static Class<?> loadFromCustomClassloader(Class<?> clazz) throws InitializationError {
        try {
            if (csl == null) {
                csl = new ProxyClassLoader();
            }
            return Class.forName(clazz.getName(), true, csl);
        } catch (ClassNotFoundException e) {
            throw new InitializationError(e);
        }
    }

    @Override
    public void run(final RunNotifier notifier) {
        Runnable runnable = () -> super.run(notifier);
        Thread thread = new Thread(runnable);
        thread.setContextClassLoader(csl);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
