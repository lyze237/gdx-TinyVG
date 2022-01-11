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

    public void setStyle(Style style, boolean flushAnyway) {
        shouldFlush = flushAnyway;
        setStyle(style);
    }

    public void setStyle(Style style) {
        if (Objects.equals(this.style, style)) {
            if (shouldFlush)
                getBatch().flush();

            return;
        }

        if (this.style != null)
            this.style.end(this);

        this.style = style;
        style.start(this);
    }

    public void flushNextStyleSwitch() {
        this.shouldFlush = true;
    }
}
