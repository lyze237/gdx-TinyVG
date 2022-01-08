package dev.lyze.gdxtinyvg.lwjgl.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.TinyVGAssetLoader;
import dev.lyze.gdxtinyvg.TinyVGIO;
import dev.lyze.gdxtinyvg.drawers.TinyVGShapeDrawer;
import dev.lyze.gdxtinyvg.lwjgl.LibgdxLwjglUnitTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class FrameBufferTest extends LibgdxLwjglUnitTest {
    private TinyVG tvg;
    private TextureRegion tvgRegion;
    private TinyVGShapeDrawer drawer;
    private Viewport viewport;

    @Override
    public void create() {
        drawer = new TinyVGShapeDrawer(new SpriteBatch(), new TextureRegion(new Texture("pixel.png")));
        viewport = new FitViewport(100, 100);
    }

    @Test
    @Tag("lwjgl")
    public void everything32() {
        setupTvg("everything-32.tvg");
    }

    private void setupTvg(String file) {
        Gdx.app.postRunnable(() -> {
            tvg = new TinyVGAssetLoader().load(file);

            viewport.setWorldSize(tvg.getWidth(), tvg.getHeight());
            viewport.getCamera().position.set(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f,
                    viewport.getCamera().position.z);
            viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            tvgRegion = TinyVGIO.toTextureRegion(tvg, drawer);
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

        tvg.draw(drawer, viewport);
        drawer.getBatch().draw(tvgRegion, 0, 0);

        drawer.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}
