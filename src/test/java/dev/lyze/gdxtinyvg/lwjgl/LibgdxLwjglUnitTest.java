package dev.lyze.gdxtinyvg.lwjgl;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import dev.lyze.gdxtinyvg.lwjgl.setup.LwjglExtension;
import dev.lyze.gdxtinyvg.lwjgl.utils.ReturnableRunnable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(LwjglExtension.class)
public class LibgdxLwjglUnitTest extends ApplicationAdapter {
    @BeforeAll
    public void init() throws InterruptedException {
        System.out.println("Before all");
        CountDownLatch latch = new CountDownLatch(1);
        Gdx.app.postRunnable(() -> {
            System.out.println("In create runnable");
            create();
            resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            LwjglExtension.getWrapper().currentAdapter = this;
            latch.countDown();
        });

        Assertions.assertTrue(latch.await(15, TimeUnit.SECONDS));
    }

    @AfterEach
    public void sleepy() throws InterruptedException {
        String timeoutEnv = System.getenv("SLEEPY");
        try {
            if (timeoutEnv != null) {
                int timeout = Integer.parseInt(timeoutEnv);
                Thread.sleep(timeout);
            }
        } catch (Exception e) {
            Thread.sleep(3000);
        }

        Thread.sleep(100);
    }

    @AfterAll
    public void cleanUp() {
        System.out.println("After all");
        dispose();
    }

    public <T> T runOnOpenGlContext(ReturnableRunnable<T> runnable, long timeout, TimeUnit timeUnit)
            throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        AtomicReference<T> value = new AtomicReference<>();
        Gdx.app.postRunnable(() -> {
            value.set(runnable.run());
            latch.countDown();
        });

        Assertions.assertTrue(latch.await(timeout, timeUnit));
        return value.get();
    }

    public void runOnOpenGlContext(Runnable runnable, long timeout, TimeUnit timeUnit) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Gdx.app.postRunnable(() -> {
            runnable.run();
            latch.countDown();
        });

        Assertions.assertTrue(latch.await(timeout, timeUnit));
    }
}
