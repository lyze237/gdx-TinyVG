package dev.lyze.gdxtinyvg.lwjgl.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.TinyVGAssetLoader;
import dev.lyze.gdxtinyvg.drawers.TinyVGShapeDrawer;
import dev.lyze.gdxtinyvg.lwjgl.LibgdxLwjglUnitTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class BoundingBoxTests extends LibgdxLwjglUnitTest {
    private TinyVG tvg;
    private TinyVGShapeDrawer drawer;
    private Viewport viewport;

    @Override
    public void create() {
        drawer = new TinyVGShapeDrawer(new SpriteBatch());
        viewport = new FitViewport(100, 100);
    }

    @Test
    @Tag("lwjgl")
    public void pirate() {
        setupTvg("pirate.tvg");
    }

    private void setupTvg(String file) {
        Gdx.app.postRunnable(() -> {
            tvg = new TinyVGAssetLoader().load(file);

            viewport = new FitViewport(tvg.getUnscaledWidth() * 2, tvg.getUnscaledHeight() * 2);
            resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        });
    }

    private float totalTime;

    private final Vector2 cursor = new Vector2();

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

        totalTime += Gdx.graphics.getDeltaTime();

        tvg.setScale(Math.abs(MathUtils.sin(totalTime)) + 1);
        tvg.setRotation(totalTime * 30);
        tvg.setShear(Math.abs(MathUtils.sin(totalTime)), 0);
        tvg.centerOrigin();

        tvg.draw(drawer);

        cursor.set(Gdx.input.getX(), Gdx.input.getY());
        tvg.drawBoundingBox(drawer, tvg.isInBoundingBox(viewport.unproject(cursor)) ? Color.GREEN : Color.RED);

        drawer.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
}
