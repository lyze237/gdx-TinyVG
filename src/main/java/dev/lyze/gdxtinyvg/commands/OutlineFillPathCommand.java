package dev.lyze.gdxtinyvg.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.LittleEndianInputStream;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.commands.paths.UnitPathCloseCommand;
import dev.lyze.gdxtinyvg.commands.paths.UnitPathSegment;
import dev.lyze.gdxtinyvg.drawers.TinyVGShapeDrawer;
import dev.lyze.gdxtinyvg.enums.CommandType;
import dev.lyze.gdxtinyvg.enums.StyleType;
import dev.lyze.gdxtinyvg.styles.Style;
import dev.lyze.gdxtinyvg.types.ParsedPathSegment;
import dev.lyze.gdxtinyvg.types.Unit;
import dev.lyze.gdxtinyvg.types.Vector2WithWidth;
import dev.lyze.gdxtinyvg.utils.StreamUtils;
import lombok.var;

import java.io.IOException;

public class OutlineFillPathCommand extends Command {
    private Style primaryStyle, secondaryStyle;
    private float startLineWidth;

    private ParsedPathSegment[] segments;

    public OutlineFillPathCommand(TinyVG tinyVG) {
        super(CommandType.FILL_PATH, tinyVG);
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
            segment.read(stream, getTinyVG().getHeader().getCoordinateRange(),
                    getTinyVG().getHeader().getFractionBits());

        segments = new ParsedPathSegment[sourceSegments.length];

        for (int i = 0; i < sourceSegments.length; i++) {
            var path = sourceSegments[i].calculatePoints(startLineWidth);
            var distinctPath = new Array<Vector2WithWidth>(path.size);

            for (var point : path) {
                if (distinctPath.size > 0 && distinctPath.get(distinctPath.size - 1).equals(point))
                    continue;

                distinctPath.add(point);
            }

            segments[i] = new ParsedPathSegment(sourceSegments[i], distinctPath);
        }
    }

    @Override
    public void draw(TinyVGShapeDrawer drawer, Viewport viewport) {

        primaryStyle.start(drawer, viewport);

        Gdx.gl.glEnable(GL20.GL_STENCIL_TEST);
        Gdx.gl.glColorMask(false, false, false, false);
        Gdx.gl.glStencilFunc(GL20.GL_ALWAYS, 0, -1);
        Gdx.gl.glStencilOp(GL20.GL_KEEP, GL20.GL_KEEP, GL20.GL_INCR);

        for (ParsedPathSegment parsedPathSegment : segments)
            drawer.filledPolygon(parsedPathSegment, getTinyVG());

        drawer.getBatch().flush();

        Gdx.gl.glColorMask(true, true, true, true);
        Gdx.gl.glStencilFunc(GL20.GL_EQUAL, 1, -1); // was GL_NOTEQUAL, 2, -1
        Gdx.gl.glStencilOp(GL20.GL_KEEP, GL20.GL_KEEP, GL20.GL_KEEP);

        for (ParsedPathSegment segment : segments)
            drawer.filledPolygon(segment.getVertices());

        drawer.getBatch().flush();
        Gdx.gl.glDisable(GL20.GL_STENCIL_TEST);
        primaryStyle.end(drawer, viewport);

        secondaryStyle.start(drawer, viewport);

        for (var segment : segments)
            drawer.path(segment, !(segment.getLastCommand() instanceof UnitPathCloseCommand), getTinyVG());

        secondaryStyle.end(drawer, viewport);

    }
}
