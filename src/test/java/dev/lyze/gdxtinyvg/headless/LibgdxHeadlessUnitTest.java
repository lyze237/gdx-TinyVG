package dev.lyze.gdxtinyvg.headless;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.graphics.GL20;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.Mockito;

public class LibgdxHeadlessUnitTest {
    private static Application application;

    @BeforeAll
    public static void init() {
        application = new HeadlessApplication(new ApplicationAdapter() {
        });
        Gdx.gl = Gdx.gl20 = Mockito.mock(GL20.class);
    }

    @AfterAll
    public static void cleanUp() {
        application.exit();
        application = null;
    }
}
