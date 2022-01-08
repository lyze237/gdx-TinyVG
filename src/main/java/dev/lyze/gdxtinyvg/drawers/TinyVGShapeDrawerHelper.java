package dev.lyze.gdxtinyvg.drawers;

import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.types.Unit;

public class TinyVGShapeDrawerHelper {

    public static float yAdjusted(Unit y, TinyVG tinyVG) {
        return yAdjusted(y.convert(), tinyVG);
    }

    public static float yAdjusted(float y, TinyVG tinyVG) {
        return tinyVG.getHeight() - scaleUnitY(y, tinyVG) + tinyVG.getPositionY();
    }

    public static float xAdjusted(Unit x, TinyVG tinyVG) {
        return xAdjusted(x.convert(), tinyVG);
    }

    public static float xAdjusted(float x, TinyVG tinyVG) {
        return scaleUnitX(x, tinyVG) + tinyVG.getPositionX();
    }

    public static float scaleUnitX(Unit x, TinyVG tinyVG) {
        return scaleUnitX(x.convert(), tinyVG);
    }

    public static float scaleUnitX(float x, TinyVG tinyVG) {
        return x * tinyVG.getScaleX();
    }

    public static float scaleUnitY(Unit y, TinyVG tinyVG) {
        return scaleUnitY(y.convert(), tinyVG);
    }

    public static float scaleUnitY(float y, TinyVG tinyVG) {
        return y * tinyVG.getScaleY();
    }

    public static float lineWidthScaled(float line, TinyVG tinyVG) {
        return line * tinyVG.getLineWidthScale();
    }
}
