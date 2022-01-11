package dev.lyze.gdxtinyvg.drawers;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.types.*;
import space.earlygrey.shapedrawer.JoinType;

public class TinyVGShapeDrawer extends GradientStyleShapeDrawer {
    public TinyVGShapeDrawer(Batch batch, TextureRegion region) {
        super(batch, region);
    }

    public void filledRectangle(UnitRectangle rectangle, TinyVG tinyVG) {
        filledRectangle(TinyVGShapeDrawerHelper.xAdjusted(rectangle.getX(), tinyVG),
                TinyVGShapeDrawerHelper.yAdjusted(rectangle.getY(), tinyVG)
                        - TinyVGShapeDrawerHelper.scaleUnitY(rectangle.getHeight(), tinyVG),
                TinyVGShapeDrawerHelper.scaleUnitX(rectangle.getWidth(), tinyVG),
                TinyVGShapeDrawerHelper.scaleUnitY(rectangle.getHeight(), tinyVG));
    }

    public void rectangle(UnitRectangle rectangle, float lineWidth, TinyVG tinyVG) {
        rectangle(TinyVGShapeDrawerHelper.xAdjusted(rectangle.getX(), tinyVG),
                TinyVGShapeDrawerHelper.yAdjusted(rectangle.getY(), tinyVG)
                        - TinyVGShapeDrawerHelper.scaleUnitY(rectangle.getHeight(), tinyVG),
                TinyVGShapeDrawerHelper.scaleUnitX(rectangle.getWidth(), tinyVG),
                TinyVGShapeDrawerHelper.scaleUnitY(rectangle.getHeight(), tinyVG),
                TinyVGShapeDrawerHelper.lineWidthScaled(lineWidth, tinyVG));
    }

    public void line(UnitLine line, float lineWidth, TinyVG tinyVG) {
        line(TinyVGShapeDrawerHelper.xAdjusted(line.getStart().getX(), tinyVG),
                TinyVGShapeDrawerHelper.yAdjusted(line.getStart().getY(), tinyVG),
                TinyVGShapeDrawerHelper.xAdjusted(line.getEnd().getX(), tinyVG),
                TinyVGShapeDrawerHelper.yAdjusted(line.getEnd().getY(), tinyVG),
                TinyVGShapeDrawerHelper.lineWidthScaled(lineWidth, tinyVG));
    }

    public void path(float[] points, float lineWidth, boolean open, TinyVG tinyVG) {
        path(points, TinyVGShapeDrawerHelper.lineWidthScaled(lineWidth, tinyVG),
                isJoinNecessary(lineWidth) ? JoinType.POINTY : JoinType.NONE, open);
    }
}
