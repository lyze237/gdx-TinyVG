package dev.lyze.gdxtinyvg.commands.paths;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.LittleEndianInputStream;
import dev.lyze.gdxtinyvg.enums.Range;
import dev.lyze.gdxtinyvg.enums.UnitPathCommandType;
import dev.lyze.gdxtinyvg.types.Unit;
import dev.lyze.gdxtinyvg.types.UnitPoint;
import dev.lyze.gdxtinyvg.types.Vector2WithWidth;
import java.io.IOException;
import lombok.var;

/**
 * The line instruction draws a straight line to the position.
 */
public class UnitPathLineCommand extends UnitPathCommand {
    /**
     * The end point of the line.
     */
    private Vector2 position;

    public UnitPathLineCommand(Unit lineWidth) {
        super(UnitPathCommandType.LINE, lineWidth);
    }

    @Override
    public void read(LittleEndianInputStream stream, Range range, int fractionBits) throws IOException {
        position = new UnitPoint(stream, range, fractionBits).convert();
    }

    @Override
    public Array<Vector2WithWidth> calculatePoints(Vector2 start, float lastLineWidth) {
        var path = new Array<Vector2WithWidth>();
        path.add(new Vector2WithWidth(start.cpy(), calculateLineWidth(lastLineWidth)));
        path.add(new Vector2WithWidth(position.cpy(), calculateLineWidth(lastLineWidth)));
        return path;
    }
}
