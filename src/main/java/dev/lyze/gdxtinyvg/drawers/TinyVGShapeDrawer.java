package dev.lyze.gdxtinyvg.drawers;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.types.Unit;
import dev.lyze.gdxtinyvg.types.UnitLine;
import dev.lyze.gdxtinyvg.types.UnitPoint;
import dev.lyze.gdxtinyvg.types.UnitRectangle;
import lombok.var;
import space.earlygrey.shapedrawer.JoinType;

public class TinyVGShapeDrawer extends GradientShapeDrawer {
    public TinyVGShapeDrawer(Batch batch, TextureRegion region) {
        super(batch, region);
    }

    public void filledRectangle(UnitRectangle rectangle, TinyVG tinyVG) {
        filledRectangle(xAdjusted(rectangle.getX(), tinyVG),
                yAdjusted(rectangle.getY(), tinyVG) - unitScaledY(rectangle.getHeight(), tinyVG),
                unitScaledX(rectangle.getWidth(), tinyVG), unitScaledY(rectangle.getHeight(), tinyVG));
    }

    public void rectangle(UnitRectangle rectangle, float lineWidth, TinyVG tinyVG) {
        rectangle(xAdjusted(rectangle.getX(), tinyVG),
                yAdjusted(rectangle.getY(), tinyVG) - unitScaledY(rectangle.getHeight(), tinyVG),
                unitScaledX(rectangle.getWidth(), tinyVG), unitScaledY(rectangle.getHeight(), tinyVG),
                lineWidthScaled(lineWidth, tinyVG));
    }

    public void line(UnitLine line, float lineWidth, TinyVG tinyVG) {
        line(xAdjusted(line.getStart().getX(), tinyVG), yAdjusted(line.getStart().getY(), tinyVG),
                xAdjusted(line.getEnd().getX(), tinyVG), yAdjusted(line.getEnd().getY(), tinyVG),
                lineWidthScaled(lineWidth, tinyVG));
    }

    public void path(Array<UnitPoint> points, float[] storage, float lineWidth, boolean open, TinyVG tinyVG) {
        path(calculateVertices(points, storage, tinyVG), lineWidthScaled(lineWidth, tinyVG),
                isJoinNecessary(lineWidth) ? JoinType.POINTY : JoinType.NONE, open);
    }

    public void pathVector(Array<Vector2> points, float[] storage, float lineWidth, boolean open, TinyVG tinyVG) {
        path(calculateVectorVertices(points, storage, tinyVG), lineWidthScaled(lineWidth, tinyVG),
                isJoinNecessary(lineWidth) ? JoinType.POINTY : JoinType.NONE, open);
    }

    public void filledPolygon(Array<UnitPoint> points, float[] storage, TinyVG tinyVG) {
        filledPolygon(calculateVertices(points, storage, tinyVG));
    }

    public void filledPolygonVector(Array<Vector2> points, float[] storage, TinyVG tinyVG) {
        filledPolygon(calculateVectorVertices(points, storage, tinyVG));
    }

    private float[] calculateVectorVertices(Array<Vector2> points, float[] storage, TinyVG tinyVG) {
        for (int p = 0, v = 0; p < points.size; p++, v += 2) {
            var point = points.get(p);

            storage[v] = xAdjusted(point.x, tinyVG);
            storage[v + 1] = yAdjusted(point.y, tinyVG);
        }

        return storage;
    }

    private float[] calculateVertices(Array<UnitPoint> points, float[] storage, TinyVG tinyVG) {
        for (int p = 0, v = 0; p < points.size; p++, v += 2) {
            var point = points.get(p);

            storage[v] = xAdjusted(point.getX(), tinyVG);
            storage[v + 1] = yAdjusted(point.getY(), tinyVG);
        }

        return storage;
    }

    private float lineWidthScaled(float line, TinyVG tinyVG) {
        return line * tinyVG.getLineWidthScale();
    }

    private float xAdjusted(float x, TinyVG tinyVG) {
        return unitScaledX(x, tinyVG) + tinyVG.getPosition().x;
    }

    private float xAdjusted(Unit x, TinyVG tinyVG) {
        return xAdjusted(x.convert(), tinyVG);
    }

    private float yAdjusted(float y, TinyVG tinyVG) {
        return tinyVG.getHeight() - unitScaledY(y, tinyVG) + tinyVG.getPosition().y;
    }

    private float yAdjusted(Unit y, TinyVG tinyVG) {
        return yAdjusted(y.convert(), tinyVG);
    }

    private float unitScaledX(float val, TinyVG tinyVG) {
        return val * tinyVG.getScale().x;
    }

    private float unitScaledX(Unit unit, TinyVG tinyVG) {
        return unitScaledX(unit.convert(), tinyVG);
    }

    private float unitScaledY(float val, TinyVG tinyVG) {
        return val * tinyVG.getScale().y;
    }

    private float unitScaledY(Unit unit, TinyVG tinyVG) {
        return unitScaledX(unit.convert(), tinyVG);
    }
}
