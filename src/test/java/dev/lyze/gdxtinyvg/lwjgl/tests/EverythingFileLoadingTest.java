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

public class EverythingFileLoadingTest extends LibgdxLwjglUnitTest {
    private TinyVG tvg, tvgScaled;
    private GradientShapeDrawer drawer;
    private Viewport viewport;

    @Override
    public void create() {
        tvg = new TinyVGAssetLoader().load("everything-32.tvg");
        tvgScaled = new TinyVGAssetLoader().load("everything-32.tvg");
        tvgScaled.getScale().set(2, 2);
        tvgScaled.getPosition().set(tvg.getWidth(), 0);
        tvgScaled.setLineWidthScale(2);

        drawer = new GradientShapeDrawer(new SpriteBatch(), new TextureRegion(new Texture("pixel.png")));
        viewport = new FitViewport(tvg.getWidth() + tvgScaled.getWidth(), tvgScaled.getHeight());
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
        drawer.setColor(Color.WHITE);
        drawer.filledRectangle(0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        tvg.draw(drawer, viewport);
        tvgScaled.draw(drawer, viewport);
        drawer.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}
