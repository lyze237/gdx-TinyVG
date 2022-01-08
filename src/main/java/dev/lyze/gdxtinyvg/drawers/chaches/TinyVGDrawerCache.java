package dev.lyze.gdxtinyvg.drawers.chaches;

import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.utils.ShortArray;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.drawers.TinyVGShapeDrawer;
import dev.lyze.gdxtinyvg.drawers.TinyVGShapeDrawerHelper;
import dev.lyze.gdxtinyvg.types.ParsedPathSegment;
import lombok.var;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class TinyVGDrawerCache {
    private final EarClippingTriangulator triangulator = new EarClippingTriangulator();

    private final TinyVG tinyVG;
    private ShortArray triangles;
    private float[] vertices;

    public TinyVGDrawerCache(TinyVG tinyVG) {
        this.tinyVG = tinyVG;
    }

    public void filledPolygon(ShapeDrawer shapeDrawer) {
        shapeDrawer.filledPolygon(vertices, triangles);
    }

    public TinyVGDrawerCache calculateVertices(ParsedPathSegment segment) {
        calculateVector(segment);

        return this;
    }

    public TinyVGDrawerCache calculateTriangles() {
        triangles = triangulator.computeTriangles(vertices, 0, vertices.length);

        return this;
    }

    public void path(TinyVGShapeDrawer shapeDrawer, float lineWidth, boolean open) {
        shapeDrawer.path(vertices, lineWidth, open, tinyVG);
    }

    protected void calculateVector(ParsedPathSegment segment) {
        vertices = new float[segment.getPoints().size * 2];
        for (int p = 0, v = 0; p < segment.getPoints().size; p++, v += 2) {
            var point = segment.getPoints().get(p);

            vertices[v] = TinyVGShapeDrawerHelper.xAdjusted(point.getPoint().x, tinyVG);
            vertices[v + 1] = TinyVGShapeDrawerHelper.yAdjusted(point.getPoint().y, tinyVG);
        }
    }
}
