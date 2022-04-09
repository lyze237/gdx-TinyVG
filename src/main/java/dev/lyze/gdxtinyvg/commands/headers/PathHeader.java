package dev.lyze.gdxtinyvg.commands.headers;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.LittleEndianInputStream;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.commands.paths.UnitPathSegment;
import dev.lyze.gdxtinyvg.enums.StyleType;
import dev.lyze.gdxtinyvg.styles.Style;
import dev.lyze.gdxtinyvg.types.ParsedPathSegment;
import dev.lyze.gdxtinyvg.types.Unit;
import dev.lyze.gdxtinyvg.types.Vector2WithWidth;
import dev.lyze.gdxtinyvg.utils.StreamUtils;
import java.io.IOException;
import lombok.Getter;
import lombok.var;

@Getter
public abstract class PathHeader {
    protected final TinyVG tinyVG;
    protected Style primaryStyle;
    protected float startLineWidth;
    protected ParsedPathSegment[] segments;

    public PathHeader(TinyVG tinyVG) {
        this.tinyVG = tinyVG;
    }

    public void read(LittleEndianInputStream stream, StyleType primaryStyleType) throws IOException {
        var count = StreamUtils.readVarUInt(stream) + 1;

        primaryStyle = primaryStyleType.read(stream, tinyVG);

        if (!shouldCalculateTriangles())
            startLineWidth = new Unit(stream, tinyVG.getHeader().getCoordinateRange(),
                    tinyVG.getHeader().getFractionBits()).convert();

        var sourceSegments = new UnitPathSegment[count];
        for (int i = 0; i < sourceSegments.length; i++)
            sourceSegments[i] = new UnitPathSegment(StreamUtils.readVarUInt(stream) + 1);

        for (UnitPathSegment segment : sourceSegments)
            segment.read(stream, getTinyVG());

        recalculateSegments(sourceSegments);
    }

    public void recalculateSegments() {
        var units = new UnitPathSegment[segments.length];
        for (int i = 0; i < segments.length; i++)
            units[i] = segments[i].getSource();

        recalculateSegments(units);
    }

    public void recalculateSegments(UnitPathSegment[] sourceSegments) {
        segments = new ParsedPathSegment[sourceSegments.length];

        for (int i = 0; i < sourceSegments.length; i++) {
            var path = sourceSegments[i].calculatePoints(startLineWidth);
            var distinctPath = new Array<Vector2WithWidth>(path.size);

            for (var point : path) {
                if (distinctPath.size > 0 && distinctPath.get(distinctPath.size - 1).equals(point))
                    continue;

                distinctPath.add(point);
            }

            segments[i] = new ParsedPathSegment(sourceSegments[i], distinctPath, tinyVG, shouldCalculateTriangles());
        }
    }

    protected abstract boolean shouldCalculateTriangles();
}
