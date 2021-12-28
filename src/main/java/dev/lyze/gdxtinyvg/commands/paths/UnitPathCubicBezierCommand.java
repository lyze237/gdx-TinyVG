package dev.lyze.gdxtinyvg.commands.paths;

import com.badlogic.gdx.math.Bezier;
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
 * The cubic bezier instruction draws a Bézier curve with two control points.
 */
public class UnitPathCubicBezierCommand extends UnitPathCommand {
    /**
     * The first control point.
     */
    private Vector2 control1;
    /**
     * The second control point.
     */
    private Vector2 control2;

    /**
     * The end point of the Bézier curve.
     */
    private Vector2 end;

    public UnitPathCubicBezierCommand(Unit lineWidth) {
        super(UnitPathCommandType.HORIZONTAL_LINE, lineWidth);
    }

    @Override
    public void read(LittleEndianInputStream stream, Range range, int fractionBits) throws IOException {
        control1 = new UnitPoint(stream, range, fractionBits).convert();
        control2 = new UnitPoint(stream, range, fractionBits).convert();
        end = new UnitPoint(stream, range, fractionBits).convert();
    }

    @Override
    public Array<Vector2WithWidth> calculatePoints(Vector2 start, float lastLineWidth) {
        var tmp = new Vector2();

        var path = new Array<Vector2WithWidth>();
        for (int i = 0; i < 100; i++) {
            var cubic = Bezier.cubic(new Vector2(), i / 100f, start, control1, control2, end, tmp);
            path.add(new Vector2WithWidth(cubic, calculateLineWidth(lastLineWidth)));
        }
        return path;
    }
}
