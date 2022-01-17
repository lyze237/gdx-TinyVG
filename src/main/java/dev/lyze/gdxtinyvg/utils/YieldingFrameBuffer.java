package dev.lyze.gdxtinyvg.utils;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import lombok.var;

public class YieldingFrameBuffer extends FrameBuffer {
    private boolean yielding = false;

    public YieldingFrameBuffer(Pixmap.Format format, int width, int height, boolean hasDepth, boolean hasStencil) {
        super(format, width, height, hasDepth, hasStencil);
    }

    @Override
    protected void disposeColorTexture(Texture t) {
        if (!yielding)
            t.dispose();
    }

    public Texture disposeAndTakeColorTexture() {
        var r = getColorBufferTexture();
        yielding = true;
        dispose();
        return r;
    }
}
