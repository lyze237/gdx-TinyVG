package dev.lyze.gdxtinyvg.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.var;

public class WhitePixelUtils {
    public static TextureRegion createWhitePixelTexture() {
        return new TextureRegion(new Texture(createWhitePixelPixmap()));
    }

    private static Pixmap createWhitePixelPixmap() {
        var pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        return pixmap;
    }
}
