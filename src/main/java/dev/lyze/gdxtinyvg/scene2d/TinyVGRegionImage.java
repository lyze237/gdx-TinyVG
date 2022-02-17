package dev.lyze.gdxtinyvg.scene2d;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.utils.Scaling;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.drawers.TinyVGShapeDrawer;

/**
 * Allows TinyVG to be implemented in Scene2D.UI Stages via an {@link Image} actor. The Image is backed by a {@link
 * TinyVGRegionDrawable} that can be automatically redrawn when {@link Widget#layout()} is called. The {@link TinyVG} is
 * rendered to a {@link TextureRegion} via a {@link com.badlogic.gdx.graphics.glutils.FrameBuffer} before it is rendered
 * to the screen. autoRedrawing is an intensive operation and should be disabled for large drawables or when many
 * TinyVGRegionImages are used in tandem. The scale of the original TinyVG is respected and automatically sets the
 * minWidth/minHeight of the Image.
 */
public class TinyVGRegionImage extends Image {
    public TinyVGRegionDrawable drawable;
    public boolean autoRedrawing = true;
    
    public TinyVGRegionImage(TinyVG tvg, TinyVGShapeDrawer shapeDrawer, int superSamplingPasses, Scaling scaling,
                             int align) {
        setDrawable(new TinyVGRegionDrawable(tvg, shapeDrawer, superSamplingPasses));
        setScaling(scaling);
        setAlign(align);
    }
    
    public TinyVGRegionImage(TinyVG tvg, TinyVGShapeDrawer shapeDrawer, int superSamplingPasses, Scaling scaling) {
        setDrawable(new TinyVGRegionDrawable(tvg, shapeDrawer, superSamplingPasses));
        setScaling(scaling);
    }
    
    public TinyVGRegionImage(TinyVG tvg, TinyVGShapeDrawer shapeDrawer, int superSamplingPasses) {
        setDrawable(new TinyVGRegionDrawable(tvg, shapeDrawer, superSamplingPasses));
    }
    
    public TinyVGRegionImage(TinyVGRegionDrawable drawable, int superSamplingPasses, Scaling scaling, int align) {
        setDrawable(drawable);
        setScaling(scaling);
        setAlign(align);
    }
    
    public TinyVGRegionImage(TinyVGRegionDrawable drawable, Scaling scaling) {
        setDrawable(drawable);
        setScaling(scaling);
    }
    
    public TinyVGRegionImage(TinyVGRegionDrawable drawable) {
        setDrawable(drawable);
    }
    
    @Override
    public void layout() {
        super.layout();
        if (isAutoRedrawing() && drawable != null) drawable.update(MathUtils.ceil(getImageWidth()), MathUtils.ceil(getImageHeight()));
    }
    
    /**
     * Sets a new drawable for the image. The image's pref size is the drawable's min size. If using the image actor's
     * size rather than the pref size, {@link #pack()} can be used to size the image to its pref size.
     *
     * @param drawable May be null.
     */
    public void setDrawable(TinyVGRegionDrawable drawable) {
        super.setDrawable(drawable);
        this.drawable = drawable;
        if (drawable != null) drawable.update();
    }
    
    public boolean isAutoRedrawing() {
        return autoRedrawing;
    }
    
    public void setAutoRedrawing(boolean autoRedrawing) {
        this.autoRedrawing = autoRedrawing;
    }
}
