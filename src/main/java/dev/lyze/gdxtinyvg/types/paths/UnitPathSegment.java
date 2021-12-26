package dev.lyze.gdxtinyvg.types.paths;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.LittleEndianInputStream;
import dev.lyze.gdxtinyvg.enums.Range;
import dev.lyze.gdxtinyvg.types.UnitPoint;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.var;

@EqualsAndHashCode
@Getter
public class UnitPathSegment {
    private final UnitPathCommand[] commands;
    private UnitPoint start;

    public UnitPathSegment(int commandCount) {
        commands = new UnitPathCommand[commandCount];
    }

    public UnitPathSegment read(LittleEndianInputStream stream, Range range, int fractionBits) throws IOException {
        start = new UnitPoint(stream, range, fractionBits);

        for (int i = 0; i < commands.length; i++)
            commands[i] = UnitPathCommand.readCommand(stream, range, fractionBits);

        return this;
    }

    public Array<Vector2> calculatePoints() {
        var point = start.convert();

        var path = new Array<Vector2>();
        for (UnitPathCommand command : commands) {
            path.addAll(command.calculatePoints(point));
            point = path.get(path.size - 1);
        }

        return path;
    }
}
