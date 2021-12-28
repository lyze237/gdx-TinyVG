package dev.lyze.gdxtinyvg.commands.paths;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.LittleEndianInputStream;
import dev.lyze.gdxtinyvg.enums.Range;
import dev.lyze.gdxtinyvg.enums.UnitPathCommandType;
import dev.lyze.gdxtinyvg.types.Unit;
import dev.lyze.gdxtinyvg.types.UnitPoint;
import dev.lyze.gdxtinyvg.types.Vector2WithWidth;
import lombok.var;

import java.io.IOException;

/**
 * Draws an ellipse segment between the current and the target point.
 */
public class UnitPathArcEllipseCommand extends UnitPathCommand {
    /**
     * If true, the large portion of the ellipse segment is drawn.
     */
    private boolean largeArc;
    /**
     * Determines if the ellipse segment is left- or right bending.
     */
    private boolean sweep;
    /**
     * The radius of the ellipse in horizontal direction.
     */
    private float radiusX;
    /**
     * The radius of the ellipse in vertical direction.
     */
    private float radiusY;
    /**
     * The rotation of the ellipse in mathematical negative direction, in degrees.
     */
    private float rotation;
    /**
     * The end point of ellipse circle segment.
     */
    private Vector2 target;

    public UnitPathArcEllipseCommand(Unit lineWidth) {
        super(UnitPathCommandType.ARC_ELLIPSE, lineWidth);
    }

    @Override
    public void read(LittleEndianInputStream stream, Range range, int fractionBits) throws IOException {
        var largeArcSweepPaddingByte = stream.readUnsignedByte();

        largeArc = (largeArcSweepPaddingByte & 0b0000_0001) == 1;
        sweep = ((largeArcSweepPaddingByte & 0b0000_0010) >> 1) == 1;

        radiusX = new Unit(stream, range, fractionBits).convert();
        radiusY = new Unit(stream, range, fractionBits).convert();

        rotation = new Unit(stream, range, fractionBits).convert();
        target = new UnitPoint(stream, range, fractionBits).convert();
    }

    @Override
    public Array<Vector2WithWidth> calculatePoints(Vector2 start, float lastLineWidth) {
        var radiusMin = start.dst(target) / 2f;
        var radiusLim = (float) Math.sqrt(radiusX * radiusX + radiusY * radiusY);

        var upScale = 1f;
        if (radiusLim < radiusMin)
            upScale = radiusMin / radiusLim;

        var ratio = radiusX / radiusY;

        var rot = rotationMat(-rotation * MathUtils.degreesToRadians);

        var transform = new float[2][2];
        transform[0][0] = rot[0][0] / upScale;
        transform[0][1] = rot[0][1] / upScale;
        transform[1][0] = rot[1][0] / upScale * ratio;
        transform[1][1] = rot[1][1] / upScale * ratio;

        var transformBack = new float[2][2];
        transformBack[0][0] = rot[1][1] * upScale;
        transformBack[0][1] = -rot[0][1] / ratio * upScale;
        transformBack[1][0] = -rot[1][0] * upScale;
        transformBack[1][1] = rot[0][0] / ratio * upScale;

        var helper = new Array<Vector2WithWidth>();

        renderCircle(helper, applyMat(transform, start), applyMat(transform, target), radiusX * upScale, largeArc,
                sweep, lastLineWidth, calculateLineWidth(lastLineWidth));

        var path = new Array<Vector2WithWidth>(helper.size);
        for (int i = 0; i < helper.size; i++) {
            path.add(new Vector2WithWidth(applyMat(transformBack, helper.get(i).getPoint()), helper.get(i).getWidth()));
        }

        return path;
    }

    private Array<Vector2WithWidth> renderCircle(Array<Vector2WithWidth> helper, Vector2 p0, Vector2 p1, float radius,
            boolean largeArc, boolean turnLeft, float startWidth, float endWidth) {
        var r = radius;

        var leftSide = (turnLeft && largeArc) || (!turnLeft && !largeArc);

        var delta = p1.cpy().sub(p0).scl(0.5f);
        var midpoint = p0.cpy().add(delta);

        Vector2 radiusVec = leftSide ? new Vector2(-delta.y, delta.x) : new Vector2(delta.y, -delta.x);

        var lenSquared = radiusVec.len2();

        if (lenSquared - 0.03f > r * r || r < 0)
            r = (float) Math.sqrt(lenSquared);

        var toCenter = radiusVec.cpy().scl((float) Math.sqrt(Math.max(0, r * r / lenSquared - 1)));
        var center = midpoint.cpy().add(toCenter);

        var angle = MathUtils.asin(MathUtils.clamp((float) Math.sqrt(lenSquared) / r, -1, 1)) * 2;
        var arc = largeArc ? MathUtils.PI2 - angle : angle;

        var pos = p0.cpy().sub(center);

        for (int i = 0; i < 100; i++) {
            var stepMat = rotationMat(i * (turnLeft ? -arc : arc) / 100);
            var point = applyMat(stepMat, pos).add(center);

            helper.add(new Vector2WithWidth(point, MathUtils.lerp(startWidth, endWidth, i / 100f)));
        }

        helper.add(new Vector2WithWidth(p1, endWidth));

        return helper;
    }

    private Vector2 applyMat(float[][] mat, Vector2 p) {
        return new Vector2(p.x * mat[0][0] + p.y * mat[0][1], p.x * mat[1][0] + p.y * mat[1][1]);
    }

    private float[][] rotationMat(float angle) {
        var s = MathUtils.sin(angle);
        var c = MathUtils.cos(angle);

        var mat = new float[2][2];
        mat[0][0] = c;
        mat[0][1] = -s;
        mat[1][0] = s;
        mat[1][1] = c;

        return mat;
    }
}
