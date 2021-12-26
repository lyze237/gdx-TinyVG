package dev.lyze.gdxtinyvg.types.paths;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.LittleEndianInputStream;
import dev.lyze.gdxtinyvg.enums.Range;
import dev.lyze.gdxtinyvg.enums.UnitPathCommandType;
import dev.lyze.gdxtinyvg.types.Unit;
import java.io.IOException;
import lombok.Getter;
import lombok.var;

public abstract class UnitPathCommand {
    @Getter private final UnitPathCommandType type;
    @Getter private final Unit hasLineWidth;

    public UnitPathCommand(UnitPathCommandType type, Unit lineWidth) {
        this.type = type;
        this.hasLineWidth = lineWidth;
    }

    public abstract void read(LittleEndianInputStream stream, Range range, int fractionBits) throws IOException;

    public abstract Array<Vector2> calculatePoints(Vector2 start);

    public static UnitPathCommand readCommand(LittleEndianInputStream stream, Range range, int fractionBits)
            throws IOException {
        var instructionLineWidthByte = stream.readUnsignedByte();

        var instruction = instructionLineWidthByte & 0b0000_0111;
        var hasLineWidth = ((instructionLineWidthByte & 0b0001_0000) >> 4) == 1;

        Unit lineWidth = null;
        if (hasLineWidth) {
            lineWidth = new Unit(stream, range, fractionBits);
        }

        return UnitPathCommandType.valueOf(instruction).read(stream, lineWidth, range, fractionBits);
    }
}
