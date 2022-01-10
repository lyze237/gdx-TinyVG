package dev.lyze.gdxtinyvg;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
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
        var batch = drawer.getBatch();
        var drawing = batch.isDrawing();
        if (drawing)
            batch.end();

        int fboWidth = MathUtils.roundPositive(Math.abs(tvg.getWidth()));
        int fboHeight = MathUtils.roundPositive(Math.abs(tvg.getHeight()));

        var fbo = new FrameBuffer(Pixmap.Format.RGBA8888, fboWidth, fboHeight, false, true);
        var viewport = new FitViewport(tvg.getWidth(), tvg.getHeight());
        viewport.update(fboWidth, fboHeight, true);

        batch.setProjectionMatrix(viewport.getCamera().combined);

        fbo.begin();

        drawer.getBatch().begin();
        tvg.draw(drawer, viewport);
        drawer.getBatch().end();

        fbo.end();

        var region = new TextureRegion(fbo.getColorBufferTexture());
        boolean flipX = tvg.getWidth() < 0;
        boolean flipY = tvg.getHeight() < 0;
        region.flip(flipX, !flipY);

        if (drawing)
            batch.begin();

        return region;
    }

    /**
     * Converts a tvg to a supersampled texture region by drawing it onto a
     * framebuffer multiple times.
     *
     * Make sure that you're not inside a batch.begin() and framebuffer.begin()
     * Takes scale into account. Be cautious of your device's VRAM and maximum
     * texture size. Each pass requires twice the width and height of the last!
     */
    @SuppressWarnings("GDXJavaFlushInsideLoop")
    public static TextureRegion toTextureRegion(TinyVG tvg, TinyVGShapeDrawer drawer, int superSamplingPasses) {
        if (superSamplingPasses < 1)
            return toTextureRegion(tvg, drawer);

        var batch = drawer.getBatch();
        var drawing = batch.isDrawing();
        if (drawing)
            batch.end();

        float initialScaleX = tvg.getScaleX();
        float initialScaleY = tvg.getScaleY();
        Texture resizedTexture = null;

        for (int i = 0; i < superSamplingPasses; i++) {
            Texture bigTexture;
            if (resizedTexture == null) {
                var scaleAmount = (float) Math.pow(2, superSamplingPasses);
                tvg.setScale(initialScaleX * scaleAmount, initialScaleY * scaleAmount);
                bigTexture = toTextureRegion(tvg, drawer).getTexture();
            } else {
                bigTexture = resizedTexture;
            }
            int width = bigTexture.getWidth();
            int height = bigTexture.getHeight();

            var fbo = new FrameBuffer(Pixmap.Format.RGBA8888, width / 2, height / 2, false, resizedTexture == null);
            var viewport = new FitViewport(fbo.getWidth(), fbo.getHeight());
            viewport.update(fbo.getWidth(), fbo.getHeight(), true);
            batch.setProjectionMatrix(viewport.getCamera().combined);

            fbo.begin();
            batch.begin();
            batch.draw(bigTexture, 0, 0, fbo.getWidth(), fbo.getHeight());
            batch.end();
            fbo.end();

            resizedTexture = fbo.getColorBufferTexture();

        }

        if (drawing)
            batch.begin();

        var resizedRegion = new TextureRegion(resizedTexture);
        resizedRegion.flip(false, superSamplingPasses % 2 == 0);
        return resizedRegion;
    }
}
