package dev.lyze.gdxtinyvg.commands.paths;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.LittleEndianInputStream;
import dev.lyze.gdxtinyvg.enums.Range;
import dev.lyze.gdxtinyvg.enums.UnitPathCommandType;
import dev.lyze.gdxtinyvg.types.Unit;
import dev.lyze.gdxtinyvg.types.Vector2WithWidth;
import lombok.var;

import java.io.IOException;

/**
 * The horizontal line instruction draws a straight horizontal line to a given x
 * coordinate.
 */
public class UnitPathHorizontalLineCommand extends UnitPathCommand {
    /**
     * The new x coordinate.
     */
    private Unit x;

    public UnitPathHorizontalLineCommand(Unit lineWidth) {
        super(UnitPathCommandType.HORIZONTAL_LINE, lineWidth);
    }

    @Override
    public void read(LittleEndianInputStream stream, Range range, int fractionBits) throws IOException {
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
