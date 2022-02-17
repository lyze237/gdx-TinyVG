package dev.lyze.gdxtinyvg.scene2d;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.TinyVGIO;
import dev.lyze.gdxtinyvg.drawers.TinyVGShapeDrawer;

/**
 * Allows TinyVG to be implemented in Scene2D.UI Stages via the Drawable
 * interface. The {@link TinyVG} is rendered to a
 * {@link TextureRegion} via a
 * {@link com.badlogic.gdx.graphics.glutils.FrameBuffer} before it is rendered
 * to the
 * screen. {@link TinyVGRegionDrawable#update()} must be called before the
 * drawable is drawn to the screen. Updating by
 * every frame is an intensive operation and not a best practice. The scale of
 * the original TinyVG is respected and
 * automatically sets the minWidth/minHeight of the Drawable
 */
public class TinyVGRegionDrawable extends BaseDrawable {
    public TinyVG tvg;
    public transient TinyVGShapeDrawer shapeDrawer;
    public TextureRegion textureRegion;
    public int superSamplingPasses;

    /**
     * A no-argument constructor necessary for serialization.
     * {@link TinyVGDrawable#shapeDrawer} must be defined before
     * this Drawable is drawn.
     */
    public TinyVGRegionDrawable() {
    }

    public TinyVGRegionDrawable(TinyVG tvg, TinyVGShapeDrawer shapeDrawer) {
        this.tvg = tvg;
        this.shapeDrawer = shapeDrawer;
    }

    public TinyVGRegionDrawable(TinyVG tvg, TinyVGShapeDrawer shapeDrawer, int superSamplingPasses) {
        this.tvg = tvg;
        this.shapeDrawer = shapeDrawer;
        this.superSamplingPasses = superSamplingPasses;
    }

    /**
     * Creates a new TinyVGDrawable with the same sizing information and tvg value
     * as the specified drawable.
     *
     * @param drawable
     */
    public TinyVGRegionDrawable(TinyVGRegionDrawable drawable) {
        super(drawable);
        tvg = drawable.tvg;
        shapeDrawer = drawable.shapeDrawer;
        superSamplingPasses = drawable.superSamplingPasses;
    }

    /**
     * Creates a new TextureRegion based on the TinyVG specified for this drawable
     * at the specified width and height.
     * Converts the tvg to a supersampled texture region by drawing it onto a
     * framebuffer multiple times. Ignores
     * Shearing. Make sure that you're not inside a batch.begin() and
     * framebuffer.begin() Takes scale into account. Be
     * cautious of your device's VRAM and maximum texture size. Each pass requires
     * twice the width and height of the
     * last!
     */
    public void update(int width, int height) {
        if (width < 1)
            width = 1;
        if (height < 1)
            height = 1;
        tvg.setSize(width, height);
        setMinSize(width, height);
        textureRegion = TinyVGIO.toTextureRegion(tvg, shapeDrawer, superSamplingPasses);
    }

    /**
     * Creates a new TextureRegion based on the TinyVG specified for this drawable
     * at the scaled width and height of the
     * original tvg. Converts the tvg to a supersampled texture region by drawing it
     * onto a framebuffer multiple times.
     * Ignores Shearing. Make sure that you're not inside a batch.begin() and
     * framebuffer.begin() Takes scale into
     * account. Be cautious of your device's VRAM and maximum texture size. Each
     * pass requires twice the width and
     * height of the last!
     */
    public void update() {
        update(MathUtils.ceil(tvg.getScaledWidth()), MathUtils.ceil(tvg.getScaledHeight()));
    }

    /**
     * @param batch
     * @param x
     * @param y
     * @param width
     * @param height
     * @throws NullPointerException
     */
    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {
        if (textureRegion == null)
            throw new NullPointerException("update() must be called before the TinyVGRegionDrawable can be drawn");
        batch.draw(textureRegion, x, y, width, height);
    }

    public TinyVG getTvg() {
        return tvg;
    }

    public void setTvg(TinyVG tvg) {
        this.tvg = tvg;
    }

    public TinyVGShapeDrawer getShapeDrawer() {
        return shapeDrawer;
    }

    public void setShapeDrawer(TinyVGShapeDrawer shapeDrawer) {
        this.shapeDrawer = shapeDrawer;
    }

    public int getSuperSamplingPasses() {
        return superSamplingPasses;
    }

    public void setSuperSamplingPasses(int superSamplingPasses) {
        this.superSamplingPasses = superSamplingPasses;
    }
}
