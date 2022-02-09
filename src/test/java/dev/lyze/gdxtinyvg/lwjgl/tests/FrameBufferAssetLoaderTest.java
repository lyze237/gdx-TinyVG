package dev.lyze.gdxtinyvg.lwjgl.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.TinyVGAssetLoader;
import dev.lyze.gdxtinyvg.TinyVGTextureAssetLoader;
import dev.lyze.gdxtinyvg.drawers.TinyVGShapeDrawer;
import dev.lyze.gdxtinyvg.lwjgl.LibgdxLwjglUnitTest;
import lombok.var;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class FrameBufferAssetLoaderTest extends LibgdxLwjglUnitTest {
    private TextureRegion tvg;
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
            var assMan = new AssetManager();
            assMan.setLogger(new Logger("aa", Logger.DEBUG));
            assMan.setLoader(TinyVG.class, new TinyVGAssetLoader());
            assMan.setLoader(TinyVGTextureAssetLoader.Result.class, new TinyVGTextureAssetLoader());

            assMan.load(file, TinyVGTextureAssetLoader.Result.class,
                    new TinyVGTextureAssetLoader.Parameters(drawer, 5, 2, 2));

            assMan.finishLoading();

            tvg = assMan.get(file, TinyVGTextureAssetLoader.Result.class).getTextureRegion();

            viewport.setWorldSize(tvg.getRegionWidth(), tvg.getRegionHeight());
            viewport.getCamera().position.set(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f,
                    viewport.getCamera().position.z);
            viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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

        drawer.getBatch().draw(tvg, 0, 0);

        drawer.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}
