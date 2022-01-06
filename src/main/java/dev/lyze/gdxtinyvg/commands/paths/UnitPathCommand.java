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
import lombok.Getter;
import lombok.var;

public abstract class UnitPathCommand {
    @Getter private final UnitPathCommandType type;
    @Getter private final Unit lineWidth;
    @Getter private final TinyVG tinyVG;

    public UnitPathCommand(UnitPathCommandType type, Unit lineWidth, TinyVG tinyVG) {
        this.type = type;
        this.lineWidth = lineWidth;
        this.tinyVG = tinyVG;
    }

    public abstract void read(LittleEndianInputStream stream) throws IOException;

    public abstract Array<Vector2WithWidth> calculatePoints(Vector2 start, float lastLineWidth);

    protected float calculateLineWidth(float lastLineWidth) {
        return lineWidth == null ? lastLineWidth : lineWidth.convert();
    }

    public static UnitPathCommand readCommand(LittleEndianInputStream stream, TinyVG tinyVG) throws IOException {
        var instructionLineWidthByte = stream.readUnsignedByte();

        var instruction = instructionLineWidthByte & 0b0000_0111;
        var hasLineWidth = ((instructionLineWidthByte & 0b0001_0000) >> 4) == 1;

        Unit lineWidth = null;
        if (hasLineWidth)
            lineWidth = new Unit(stream, tinyVG.getHeader().getCoordinateRange(), tinyVG.getHeader().getFractionBits());

        return UnitPathCommandType.valueOf(instruction).read(stream, lineWidth, tinyVG);
    }
}
