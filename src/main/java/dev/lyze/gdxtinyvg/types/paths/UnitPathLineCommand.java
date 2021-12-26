package dev.lyze.gdxtinyvg.types.paths;

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
 * The line instruction draws a straight line to the position.
 */
public class UnitPathLineCommand extends UnitPathCommand {
    /**
     * The end point of the line.
     */
    private UnitPoint position;

    public UnitPathLineCommand(Unit lineWidth) {
        super(UnitPathCommandType.LINE, lineWidth);
    }

    @Override
    public void read(LittleEndianInputStream stream, Range range, int fractionBits) throws IOException {
        position = new UnitPoint(stream, range, fractionBits);
    }

    @Override
    public Array<Vector2> calculatePoints(Vector2 start) {
        var path = new Array<Vector2>();
        path.add(start.cpy());
        path.add(position.convert());
        return path;
    }
}
