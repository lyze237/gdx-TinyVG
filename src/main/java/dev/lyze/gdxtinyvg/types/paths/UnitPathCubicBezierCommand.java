package dev.lyze.gdxtinyvg.types.paths;

import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.LittleEndianInputStream;
import dev.lyze.gdxtinyvg.enums.Range;
import dev.lyze.gdxtinyvg.enums.UnitPathCommandType;
import dev.lyze.gdxtinyvg.types.Unit;
import dev.lyze.gdxtinyvg.types.UnitPoint;
import java.io.IOException;
import lombok.var;

/**
 * The cubic bezier instruction draws a Bézier curve with two control points.
 */
public class UnitPathCubicBezierCommand extends UnitPathCommand {
    /**
     * The first control point.
     */
    private UnitPoint control1;
    /**
     * The second control point.
     */
    private UnitPoint control2;

    /**
     * The end point of the Bézier curve.
     */
    private UnitPoint end;

    public UnitPathCubicBezierCommand(Unit lineWidth) {
        super(UnitPathCommandType.HORIZONTAL_LINE, lineWidth);
    }

    @Override
    public void read(LittleEndianInputStream stream, Range range, int fractionBits) throws IOException {
        control1 = new UnitPoint(stream, range, fractionBits);
        control2 = new UnitPoint(stream, range, fractionBits);
        end = new UnitPoint(stream, range, fractionBits);
    }

    @Override
    public Array<Vector2> calculatePoints(Vector2 start) {
        var tmp = new Vector2();
        var startVector = start.cpy();
        var endVector = end.convert();
        var control1Vector = control1.convert();
        var control2Vector = control2.convert();

        var path = new Array<Vector2>();
        for (int i = 0; i < 100; i++) {
            var cubic = Bezier.cubic(new Vector2(), i / 100f, startVector, control1Vector, control2Vector, endVector,
                    tmp);
            path.add(cubic);
        }
        return path;
    }
}
