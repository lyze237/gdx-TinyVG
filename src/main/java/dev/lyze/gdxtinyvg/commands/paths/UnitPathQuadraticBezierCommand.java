package dev.lyze.gdxtinyvg.commands.paths;

import com.badlogic.gdx.math.Bezier;
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
 * The quadratic bezier instruction draws a Bézier curve with a single control
 * point.
 */
public class UnitPathQuadraticBezierCommand extends UnitPathCommand {
    /**
     * The control point.
     */
    private UnitPoint control1;

    /**
     * The end point of the Bézier curve.
     */
    private UnitPoint end;

    public UnitPathQuadraticBezierCommand(Unit lineWidth, TinyVG tinyVG) {
        super(UnitPathCommandType.HORIZONTAL_LINE, lineWidth, tinyVG);
    }

    @Override
    public void read(LittleEndianInputStream stream) throws IOException {
        var range = getTinyVG().getHeader().getCoordinateRange();
        var fractionBits = getTinyVG().getHeader().getFractionBits();

        control1 = new UnitPoint(stream, range, fractionBits);
        end = new UnitPoint(stream, range, fractionBits);
    }

    @Override
    public Array<Vector2WithWidth> calculatePoints(Vector2 start, float lastLineWidth) {
        var tmp = new Vector2();
        var startVector = start.cpy();
        var endVector = end.convert();
        var control1Vector = control1.convert();

        var path = new Array<Vector2WithWidth>();
        for (int i = 0; i < getTinyVG().getCurvePoints(); i++)
            path.add(new Vector2WithWidth(Bezier.quadratic(new Vector2(), (float) i / getTinyVG().getCurvePoints(),
                    startVector, control1Vector, endVector, tmp), calculateLineWidth(lastLineWidth)));

        return path;
    }
}
