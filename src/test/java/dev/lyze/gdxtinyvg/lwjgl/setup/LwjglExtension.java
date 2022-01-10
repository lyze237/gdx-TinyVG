package dev.lyze.gdxtinyvg.lwjgl.setup;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.var;
import org.junit.jupiter.api.Assertions;

public class LwjglExtension extends BaseLwjglExtension {
    private static final CountDownLatch lock = new CountDownLatch(1);

    @Getter private static Lwjgl3Application application;
    @Getter private static ApplicationAdapterWrapper wrapper;

    @SneakyThrows
    @Override
    void setup() {
        System.out.println("Setup");
        wrapper = new ApplicationAdapterWrapper(new ApplicationAdapter() {
            @Override
            public void create() {
                System.out.println("lock down created");
                lock.countDown();
            }
        });

        var config = new Lwjgl3ApplicationConfiguration();
        config.setBackBufferConfig(8, 8, 8, 8, 16, 2, 0);

        new Thread(() -> application = new Lwjgl3Application(wrapper, config)).start();

        Assertions.assertTrue(lock.await(5, TimeUnit.SECONDS));
    }

    @Override
    public void close() {
        System.out.println("Close");
        if (application != null)
            application.exit();
    }
}
