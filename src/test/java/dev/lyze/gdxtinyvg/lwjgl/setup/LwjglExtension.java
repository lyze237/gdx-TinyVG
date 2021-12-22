package dev.lyze.gdxtinyvg.lwjgl.setup;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import org.junit.jupiter.api.Assertions;

public class LwjglExtension extends BaseLwjglExtension {
    private static final CountDownLatch lock = new CountDownLatch(1);

    @Getter private static LwjglApplication application;
    @Getter private static ApplicationAdapterWrapper wrapper;

    @Override
    void setup() throws InterruptedException {
        System.out.println("Setup");
        wrapper = new ApplicationAdapterWrapper(new ApplicationAdapter() {
            @Override
            public void create() {
                lock.countDown();
            }
        });

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.vSyncEnabled = true;
        config.width = 1280;
        config.height = 720;

        application = new LwjglApplication(wrapper, config);

        Assertions.assertTrue(lock.await(5, TimeUnit.SECONDS));
    }

    @Override
    public void close() {
        System.out.println("Close");
        application.exit();
    }
}
