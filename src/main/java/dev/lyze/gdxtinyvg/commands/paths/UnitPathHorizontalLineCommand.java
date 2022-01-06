package dev.lyze.gdxtinyvg.commands.paths;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.LittleEndianInputStream;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.enums.Range;
import dev.lyze.gdxtinyvg.enums.UnitPathCommandType;
import dev.lyze.gdxtinyvg.types.Unit;
import dev.lyze.gdxtinyvg.types.Vector2WithWidth;
import java.io.IOException;
import lombok.var;

/**
 * The horizontal line instruction draws a straight horizontal line to a given x
 * coordinate.
 */
public class UnitPathHorizontalLineCommand extends UnitPathCommand {
    /**
     * The new x coordinate.
     */
    private Unit x;

    public UnitPathHorizontalLineCommand(Unit lineWidth, TinyVG tinyVG) {
        super(UnitPathCommandType.HORIZONTAL_LINE, lineWidth, tinyVG);
    }

    @Override
    public void read(LittleEndianInputStream stream) throws IOException {
        var range = getTinyVG().getHeader().getCoordinateRange();
        var fractionBits = getTinyVG().getHeader().getFractionBits();

        x = new Unit(stream, range, fractionBits);
    }

    @Override
    public Array<Vector2WithWidth> calculatePoints(Vector2 start, float lastLineWidth) {
        var path = new Array<Vector2WithWidth>();
        path.add(new Vector2WithWidth(start.cpy(), calculateLineWidth(lastLineWidth)));
        path.add(new Vector2WithWidth(start.cpy().set(x.convert(), start.y), calculateLineWidth(lastLineWidth)));
        return path;
    }
}
