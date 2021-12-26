package dev.lyze.gdxtinyvg.lwjgl.tests;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.TinyVGAssetLoader;
import dev.lyze.gdxtinyvg.drawers.TinyVGShapeDrawer;
import dev.lyze.gdxtinyvg.lwjgl.LibgdxLwjglUnitTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class TestFileLoadingTest extends LibgdxLwjglUnitTest {
    private TinyVG tvg, tvgScaled;
    private TinyVGShapeDrawer drawer;
    private Viewport viewport;

    @Override
    public void create() {
        tvg = new TinyVGAssetLoader().load("test.tvg");
        tvgScaled = new TinyVGAssetLoader().load("test.tvg");
        tvgScaled.getScale().set(2, 2);

        drawer = new TinyVGShapeDrawer(new SpriteBatch(), new TextureRegion(new Texture("pixel.png")));
        viewport = new ExtendViewport(tvg.getHeader().getWidth() * 2, tvg.getHeader().getHeight() * 2);
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
