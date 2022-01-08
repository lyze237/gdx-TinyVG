package dev.lyze.gdxtinyvg;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import dev.lyze.gdxtinyvg.drawers.TinyVGShapeDrawer;
import lombok.var;

public class TinyVGIO {
    /**
     * Converts a tvg to a texture region by drawing it onto a framebuffer.
     *
     * Make sure that you're not inside a batch.begin() and framebuffer.begin()
     * Takes scale into account.
     */
    public static TextureRegion toTextureRegion(TinyVG tvg, TinyVGShapeDrawer drawer) {
        var drawing = drawer.getBatch().isDrawing();
        if (drawing)
            drawer.getBatch().end();

        var buffer = new FrameBuffer(Pixmap.Format.RGBA8888, (int) tvg.getWidth(), (int) tvg.getHeight(), false);
        var viewport = new FitViewport(buffer.getWidth(), buffer.getHeight());
        viewport.update(buffer.getWidth(), buffer.getHeight(), true);

        viewport.apply();
        drawer.getBatch().setProjectionMatrix(viewport.getCamera().combined);

        buffer.begin();

        ScreenUtils.clear(Color.CLEAR);
        drawer.getBatch().begin();
        tvg.draw(drawer, viewport);
        drawer.getBatch().end();

        buffer.end();

        var region = new TextureRegion(buffer.getColorBufferTexture());
        region.flip(false, true);

        if (drawing)
            drawer.getBatch().begin();

        return region;
    }
}
