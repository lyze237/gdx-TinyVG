package dev.lyze.gdxtinyvg.commands.paths;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.LittleEndianInputStream;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.enums.Range;
import dev.lyze.gdxtinyvg.enums.UnitPathCommandType;
import dev.lyze.gdxtinyvg.types.Unit;
import dev.lyze.gdxtinyvg.types.UnitPoint;
import dev.lyze.gdxtinyvg.types.Vector2WithWidth;
import java.io.IOException;
import lombok.var;

/**
 * Draws an circle segment between the current and the target point.
 */
public class UnitPathArcCircleCommand extends UnitPathCommand {
    /**
     * If true, the large portion of the circle segment is drawn.
     */
    private boolean largeArc;
    /**
     * Determines if the circle segment is left- or right bending.
     */
    private boolean sweep;
    /**
     * The radius of the circle.
     */
    private float radius;
    /**
     * The end point of ellipse circle segment.
     */
    private Vector2 target;

    public UnitPathArcCircleCommand(Unit lineWidth, TinyVG tinyVG) {
        super(UnitPathCommandType.ARC_CIRCLE, lineWidth, tinyVG);
    }

    @Override
    public void read(LittleEndianInputStream stream) throws IOException {
        var range = getTinyVG().getHeader().getCoordinateRange();
        var fractionBits = getTinyVG().getHeader().getFractionBits();

        var largeArcSweepPaddingByte = stream.readUnsignedByte();

        largeArc = (largeArcSweepPaddingByte & 0b0000_0001) == 1;
        sweep = ((largeArcSweepPaddingByte & 0b0000_0010) >> 1) == 1;

        radius = new Unit(stream, range, fractionBits).convert();

        target = new UnitPoint(stream, range, fractionBits).convert();
    }

    @Override
    public Array<Vector2WithWidth> calculatePoints(Vector2 start, float lastLineWidth) {
        var path = new Array<Vector2WithWidth>();

        renderCircle(path, start, target, radius, largeArc, sweep, lastLineWidth, calculateLineWidth(lastLineWidth));

        return path;
    }

    private Array<Vector2WithWidth> renderCircle(Array<Vector2WithWidth> path, Vector2 p0, Vector2 p1, float radius,
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

        for (int i = 0; i < getTinyVG().getCurvePoints(); i++) {
            var stepMat = rotationMat(i * (turnLeft ? -arc : arc) / getTinyVG().getCurvePoints());
            var point = applyMat(stepMat, pos).add(center);

            path.add(new Vector2WithWidth(point,
                    MathUtils.lerp(startWidth, endWidth, (float) i / getTinyVG().getCurvePoints())));
        }

        path.add(new Vector2WithWidth(p1, endWidth));

        return path;
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
