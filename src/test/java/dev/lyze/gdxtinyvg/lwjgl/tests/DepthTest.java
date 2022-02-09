package dev.lyze.gdxtinyvg.lwjgl.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.TinyVGAssetLoader;
import dev.lyze.gdxtinyvg.drawers.TinyVGShapeDrawer;
import dev.lyze.gdxtinyvg.lwjgl.LibgdxLwjglUnitTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class DepthTest extends LibgdxLwjglUnitTest {
    private TinyVG tvg;
    private TinyVGShapeDrawer drawer;
    private Viewport viewport;

    private GLProfiler profiler;

    @Override
    public void create() {
        drawer = new TinyVGShapeDrawer(new SpriteBatch());
        viewport = new FitViewport(100, 100);

        profiler = new GLProfiler(Gdx.graphics);
        profiler.enable();
    }

    @Test
    @Tag("lwjgl")
    public void pirate() {
        setupTvg("button-nw.tvg");
    }

    private void setupTvg(String file) {
        Gdx.app.postRunnable(() -> {
            tvg = new TinyVGAssetLoader().load(file);

            viewport = new FitViewport(tvg.getScaledWidth() * 5, tvg.getScaledHeight() * 5);
            tvg.centerOrigin();
            tvg.setOriginBasedPosition(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f);
            resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        });
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (tvg == null)
            return;

        viewport.apply();

        drawer.getBatch().setProjectionMatrix(viewport.getCamera().combined);

        drawer.getBatch().begin();
        drawer.filledRectangle(0, 0, viewport.getWorldWidth(), viewport.getWorldHeight(), Color.TEAL);

        profiler.reset();
        tvg.draw(drawer);
        System.out.println(profiler.getDrawCalls());

        drawer.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}
