package dev.lyze.gdxtinyvg.commands.headers;

import com.badlogic.gdx.utils.LittleEndianInputStream;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.commands.paths.UnitPathSegment;
import dev.lyze.gdxtinyvg.enums.StyleType;
import dev.lyze.gdxtinyvg.styles.Style;
import dev.lyze.gdxtinyvg.types.Unit;
import dev.lyze.gdxtinyvg.utils.StreamUtils;
import java.io.IOException;
import lombok.Getter;
import lombok.var;

@Getter
public class OutlineFillPathHeader extends PathHeader {
    private Style secondaryStyle;

    public OutlineFillPathHeader(TinyVG tinyVG) {
        super(tinyVG);
    }

    @Override
    public void read(LittleEndianInputStream stream, StyleType primaryStyleType) throws IOException {
        var countSecondaryStyleByte = stream.readUnsignedByte();
        var count = (countSecondaryStyleByte & 0b0011_1111) + 1;
        var secondaryStyleType = StyleType.valueOf((countSecondaryStyleByte & 0b1100_0000) >> 6);

        primaryStyle = primaryStyleType.read(stream, getTinyVG());
        secondaryStyle = secondaryStyleType.read(stream, getTinyVG());

        startLineWidth = new Unit(stream, getTinyVG().getHeader().getCoordinateRange(),
                getTinyVG().getHeader().getFractionBits()).convert();

        var sourceSegments = new UnitPathSegment[count];
        for (int i = 0; i < sourceSegments.length; i++)
            sourceSegments[i] = new UnitPathSegment(StreamUtils.readVarUInt(stream) + 1);

        for (UnitPathSegment segment : sourceSegments)
            segment.read(stream, getTinyVG());

        recalculateSegments(sourceSegments);
    }

    @Override
    protected boolean shouldCalculateTriangles() {
        return true;
    }
}
