package dev.lyze.gdxtinyvg.drawers;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.types.*;
import lombok.var;
import space.earlygrey.shapedrawer.JoinType;

public class TinyVGShapeDrawer extends GradientShapeDrawer {
    private final EarClippingTriangulator triangulator = new EarClippingTriangulator();

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

    public void path(Array<UnitPoint> points, float[] storage, float lineWidth, boolean open, TinyVG tinyVG) {
        path(calculateVertices(points, storage, tinyVG), TinyVGShapeDrawerHelper.lineWidthScaled(lineWidth, tinyVG),
                isJoinNecessary(lineWidth) ? JoinType.POINTY : JoinType.NONE, open);
    }

    public void path(float[] points, float lineWidth, boolean open, TinyVG tinyVG) {
        path(points, TinyVGShapeDrawerHelper.lineWidthScaled(lineWidth, tinyVG),
                isJoinNecessary(lineWidth) ? JoinType.POINTY : JoinType.NONE, open);
    }

    public void filledPolygon(Array<UnitPoint> points, float[] storage, TinyVG tinyVG) {
        filledPolygon(calculateVertices(points, storage, tinyVG));
    }

    private float[] calculateVectorVertices(Array<Vector2> points, float[] storage, TinyVG tinyVG) {
        for (int p = 0, v = 0; p < points.size; p++, v += 2) {
            var point = points.get(p);

            storage[v] = TinyVGShapeDrawerHelper.xAdjusted(point.x, tinyVG);
            storage[v + 1] = TinyVGShapeDrawerHelper.yAdjusted(point.y, tinyVG);
        }

        return storage;
    }

    private float[] calculateVertices(Array<UnitPoint> points, float[] storage, TinyVG tinyVG) {
        for (int p = 0, v = 0; p < points.size; p++, v += 2) {
            var point = points.get(p);

            storage[v] = TinyVGShapeDrawerHelper.xAdjusted(point.getX().convert(), tinyVG);
            storage[v + 1] = TinyVGShapeDrawerHelper.yAdjusted(point.getY().convert(), tinyVG);
        }

        return storage;
    }
}
