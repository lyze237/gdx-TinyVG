package dev.lyze.gdxtinyvg.lwjgl.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.TinyVGAssetLoader;
import dev.lyze.gdxtinyvg.drawers.TinyVGShapeDrawer;
import dev.lyze.gdxtinyvg.lwjgl.LibgdxLwjglUnitTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class ExamplesFileLoadingTest extends LibgdxLwjglUnitTest {
    private TinyVG tvg, tvgScaled;
    private TinyVGShapeDrawer drawer;
    private Viewport viewport;

    private GLProfiler profiler;

    @Override
    public void create() {
        drawer = new TinyVGShapeDrawer(new SpriteBatch(), new TextureRegion(new Texture("pixel.png")));
        viewport = new FitViewport(100, 100);

        profiler = new GLProfiler(Gdx.graphics);
        profiler.enable();
    }

    @Test
    @Tag("lwjgl")
    public void everything32() {
        setupTvg("everything-32.tvg");
    }

    @Test
    @Tag("lwjgl")
    public void pirate() {
        setupTvg("pirate.tvg");
    }

    @Test
    @Tag("lwjgl")
    public void shield() {
        setupTvg("shield.tvg");
    }

    @Test
    @Tag("lwjgl")
    public void flowchart() {
        setupTvg("flowchart.tvg");
    }

    @Test
    @Tag("lwjgl")
    public void tiger() {
        setupTvg("tiger.tvg");
    }

    @Test
    @Tag("lwjgl")
    public void comic() {
        setupTvg("comic.tvg");
    }

    @Test
    @Tag("lwjgl")
    public void chart() {
        setupTvg("chart.tvg");
    }

    @Test
    @Tag("lwjgl")
    public void appIcon() {
        setupTvg("app-icon.tvg");
    }

    private void setupTvg(String file) {
        Gdx.app.postRunnable(() -> {
            tvg = new TinyVGAssetLoader().load(file);
            tvgScaled = new TinyVGAssetLoader().load(file);
            tvgScaled.setScale(2);
            tvgScaled.setPosition(tvg.getWidth(), 0);
            tvgScaled.setLineWidthScale(2);

            viewport.setWorldSize(tvg.getWidth() + tvgScaled.getWidth(), tvgScaled.getHeight());
            viewport.getCamera().position.set(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f,
                    viewport.getCamera().position.z);
            viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        });
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_STENCIL_BUFFER_BIT);

        if (tvg == null)
            return;

        viewport.apply();

        drawer.getBatch().setProjectionMatrix(viewport.getCamera().combined);

        drawer.getBatch().begin();
        drawer.filledRectangle(0, 0, viewport.getWorldWidth(), viewport.getWorldHeight(), Color.TEAL);

        profiler.reset();
        tvg.draw(drawer, viewport);
        System.out.println(profiler.getDrawCalls());
        tvgScaled.draw(drawer, viewport);

        drawer.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}
