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
        filledRectangle(rectangle.getX().convert(), adjustY(rectangle.getY(), tinyVG) - rectangle.getHeight().convert(),
                rectangle.getWidth().convert(), rectangle.getHeight().convert());
    }

    public void rectangle(UnitRectangle rectangle, float lineWidth, TinyVG tinyVG) {
        rectangle(rectangle.getX().convert(), adjustY(rectangle.getY(), tinyVG) - rectangle.getHeight().convert(),
                rectangle.getWidth().convert(), rectangle.getHeight().convert(), lineWidth);
    }

    public void line(UnitLine line, float lineWidth, TinyVG tinyVG) {
        line(line.getStart().getX().convert(), adjustY(line.getStart().getY(), tinyVG), line.getEnd().getX().convert(),
                adjustY(line.getEnd().getY(), tinyVG), lineWidth);
    }

    public void path(float[] points, float lineWidth, boolean open) {
        path(points, lineWidth, isJoinNecessary(lineWidth) ? JoinType.POINTY : JoinType.NONE, open);
    }

    public static float adjustY(Unit y, TinyVG tinyVG) {
        return adjustY(y.convert(), tinyVG);
    }

    public static float adjustY(float y, TinyVG tinyVG) {
        return tinyVG.getHeight() - y;
    }
}
