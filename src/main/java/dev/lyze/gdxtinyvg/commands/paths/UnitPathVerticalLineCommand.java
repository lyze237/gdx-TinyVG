package dev.lyze.gdxtinyvg.commands.paths;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.LittleEndianInputStream;
import dev.lyze.gdxtinyvg.enums.Range;
import dev.lyze.gdxtinyvg.enums.UnitPathCommandType;
import dev.lyze.gdxtinyvg.types.Unit;
import dev.lyze.gdxtinyvg.types.Vector2WithWidth;
import java.io.IOException;
import lombok.var;

/**
 * The vertical line instruction draws a straight horizontal line to a given x
 * coordinate.
 */
public class UnitPathVerticalLineCommand extends UnitPathCommand {
    /**
     * The new y coordinate.
     */
    private Unit y;

    public UnitPathVerticalLineCommand(Unit lineWidth) {
        super(UnitPathCommandType.VERTICAL_LINE, lineWidth);
    }

    @Override
    public void read(LittleEndianInputStream stream, Range range, int fractionBits) throws IOException {
        y = new Unit(stream, range, fractionBits);
    }

    @Override
    public Array<Vector2WithWidth> calculatePoints(Vector2 start, float lastLineWidth) {
        var path = new Array<Vector2WithWidth>();
        path.add(new Vector2WithWidth(start.cpy(), calculateLineWidth(lastLineWidth)));
        path.add(new Vector2WithWidth(start.cpy().set(start.x, y.convert()), calculateLineWidth(lastLineWidth)));
        return path;
    }
}
