package dev.lyze.gdxtinyvg.lwjgl.tests;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.GradientShapeDrawer;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.TinyVGAssetLoader;
import dev.lyze.gdxtinyvg.lwjgl.LibgdxLwjglUnitTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class BasicFileLoadingTest extends LibgdxLwjglUnitTest {
    private TinyVG tvg;
    private GradientShapeDrawer drawer;
    private Viewport viewport;

    @Override
    public void create() {
        tvg = new TinyVGAssetLoader().load("square.tvg");

        drawer = new GradientShapeDrawer(new SpriteBatch(), new TextureRegion(new Texture("pixel.png")));
        viewport = new FitViewport(tvg.getHeader().getWidth(), tvg.getHeader().getHeight());
    }

    @Test
    @Tag("lwjgl")
    public void test() {

    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);

        viewport.apply();

        drawer.getBatch().setProjectionMatrix(viewport.getCamera().combined);

        drawer.getBatch().begin();
        tvg.draw(drawer, viewport);
        drawer.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}
