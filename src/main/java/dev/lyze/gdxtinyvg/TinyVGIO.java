package dev.lyze.gdxtinyvg;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import dev.lyze.gdxtinyvg.drawers.TinyVGShapeDrawer;
import dev.lyze.gdxtinyvg.utils.YieldingFrameBuffer;
import lombok.var;

public class TinyVGIO {
    /**
     * Converts a tvg to a texture region by drawing it onto a framebuffer.
     * Ignores Shearing.
     *
     * Make sure that you're not inside a batch.begin() and framebuffer.begin()
     * Takes scale into account.
     */
    public static TextureRegion toTextureRegion(TinyVG tvg, TinyVGShapeDrawer drawer) {
        var batch = drawer.getBatch();
        var drawing = batch.isDrawing();
        if (drawing)
            batch.end();

        var shearX = tvg.getShearX();
        var shearY = tvg.getShearY();
        tvg.setShear(0, 0);

        var fboWidth = MathUtils.roundPositive(Math.abs(tvg.getScaledWidth()));
        var fboHeight = MathUtils.roundPositive(Math.abs(tvg.getScaledHeight()));

        var fbo = new YieldingFrameBuffer(Pixmap.Format.RGBA8888, fboWidth, fboHeight, false, true);
        var viewport = new FitViewport(tvg.getScaledWidth(), tvg.getScaledHeight());
        viewport.update(fboWidth, fboHeight, true);

        batch.setProjectionMatrix(viewport.getCamera().combined);

        fbo.begin();
        ScreenUtils.clear(Color.CLEAR);

        drawer.getBatch().begin();
        tvg.draw(drawer);
        drawer.getBatch().end();

        fbo.end();

        var region = new TextureRegion(fbo.disposeAndTakeColorTexture());
        boolean flipX = tvg.getScaledWidth() < 0;
        boolean flipY = tvg.getScaledHeight() < 0;

        region.flip(flipX, !flipY);

        if (drawing)
            batch.begin();

        tvg.setShear(shearX, shearY);

        return region;
    }

    /**
     * Converts a tvg to a supersampled texture region by drawing it onto a
     * framebuffer multiple times. Ignores Shearing.
     *
     * Make sure that you're not inside a batch.begin() and framebuffer.begin()
     * Takes scale into account. Be cautious of your device's VRAM and maximum
     * texture size. Each pass requires twice the width and height of the last!
     */
    @SuppressWarnings("GDXJavaFlushInsideLoop")
    public static TextureRegion toTextureRegion(TinyVG tvg, TinyVGShapeDrawer drawer, int supersamplingPasses) {
        if (supersamplingPasses < 1)
            return toTextureRegion(tvg, drawer);

        var batch = drawer.getBatch();
        var drawing = batch.isDrawing();
        if (drawing)
            batch.end();

        var initialScaleX = tvg.getScaleX();
        var initialScaleY = tvg.getScaleY();

        var shearX = tvg.getShearX();
        var shearY = tvg.getShearY();
        tvg.setShear(0, 0);

        Texture resizedTexture = null;

        for (int i = 0; i < supersamplingPasses; i++) {
            Texture bigTexture;
            if (resizedTexture == null) {
                var scaleAmount = (float) Math.pow(2, supersamplingPasses);
                tvg.setScale(initialScaleX * scaleAmount, initialScaleY * scaleAmount);

                bigTexture = toTextureRegion(tvg, drawer).getTexture();
            } else {
                bigTexture = resizedTexture;
            }
            var width = bigTexture.getWidth();
            var height = bigTexture.getHeight();

            var fbo = new YieldingFrameBuffer(Pixmap.Format.RGBA8888, width / 2, height / 2, false, true);

            var viewport = new FitViewport(fbo.getWidth(), fbo.getHeight());
            viewport.update(fbo.getWidth(), fbo.getHeight(), true);
            batch.setProjectionMatrix(viewport.getCamera().combined);

            fbo.begin();
            ScreenUtils.clear(Color.CLEAR);

            batch.begin();
            batch.draw(bigTexture, 0, 0, fbo.getWidth(), fbo.getHeight());
            batch.end();

            fbo.end();

            resizedTexture = fbo.disposeAndTakeColorTexture();
        }

        if (drawing)
            batch.begin();

        var resizedRegion = new TextureRegion(resizedTexture);
        resizedRegion.flip(false, supersamplingPasses % 2 == 0);

        tvg.setShear(shearX, shearY);

        return resizedRegion;
    }
}
