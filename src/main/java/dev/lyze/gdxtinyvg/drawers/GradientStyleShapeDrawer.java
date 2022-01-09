package dev.lyze.gdxtinyvg.drawers;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.styles.Style;
import java.util.Objects;

public class GradientStyleShapeDrawer extends GradientShapeDrawer {
    private Style style;
    private boolean shouldFlush;

    public GradientStyleShapeDrawer(Batch batch, TextureRegion region) {
        super(batch, region);
    }

    public void setStyle(Style style, Viewport viewport, boolean flushAnyway) {
        shouldFlush = flushAnyway;
        setStyle(style, viewport);
    }

    public void setStyle(Style style, Viewport viewport) {
        if (Objects.equals(this.style, style)) {
            if (shouldFlush)
                getBatch().flush();

            return;
        }

        if (this.style != null)
            this.style.end(this, viewport);

        this.style = style;
        style.start(this, viewport);
    }

    public void flushNextStyleSwitch() {
        this.shouldFlush = true;
    }
}
