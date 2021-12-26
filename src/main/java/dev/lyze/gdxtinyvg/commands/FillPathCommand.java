package dev.lyze.gdxtinyvg.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.LittleEndianInputStream;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.drawers.TinyVGShapeDrawer;
import dev.lyze.gdxtinyvg.enums.CommandType;
import dev.lyze.gdxtinyvg.enums.StyleType;
import dev.lyze.gdxtinyvg.styles.Style;
import dev.lyze.gdxtinyvg.types.paths.UnitPathSegment;
import dev.lyze.gdxtinyvg.utils.StreamUtils;
import java.io.IOException;
import lombok.var;

public class FillPathCommand extends Command {
    private Style primaryStyle;
    private Array<Array<Vector2>> parsedSegments;
    private float[][] segmentVertices;

    public FillPathCommand(TinyVG tinyVG) {
        super(CommandType.FILL_PATH, tinyVG);
    }

    @Override
    public void read(LittleEndianInputStream stream, StyleType primaryStyleType) throws IOException {
        var count = StreamUtils.readVarUInt(stream) + 1;

        primaryStyle = primaryStyleType.read(stream, getTinyVG());

        var segments = new UnitPathSegment[count];
        for (int i = 0; i < segments.length; i++)
            segments[i] = new UnitPathSegment(StreamUtils.readVarUInt(stream) + 1);

        for (UnitPathSegment segment : segments)
            segment.read(stream, getTinyVG().getHeader().getCoordinateRange(),
                    getTinyVG().getHeader().getFractionBits());

        parsedSegments = new Array<>(segments.length);
        segmentVertices = new float[segments.length][];

        for (int i = 0; i < segments.length; i++) {
            var path = segments[i].calculatePoints();
            var distinctPath = new Array<Vector2>(path.size);

            for (Vector2 point : path) {
                if (distinctPath.size > 0 && distinctPath.get(distinctPath.size - 1).equals(point))
                    continue;

                distinctPath.add(point);
            }

            parsedSegments.add(distinctPath);
            segmentVertices[i] = new float[distinctPath.size * 2];
        }
    }

    @Override
    public void draw(TinyVGShapeDrawer drawer, Viewport viewport) {
        primaryStyle.start(drawer, viewport);

        Gdx.gl.glEnable(GL20.GL_STENCIL_TEST);
        Gdx.gl.glColorMask(false, false, false, false);
        Gdx.gl.glStencilFunc(GL20.GL_ALWAYS, 0, -1);
        Gdx.gl.glStencilOp(GL20.GL_KEEP, GL20.GL_KEEP, GL20.GL_INCR);

        for (int i = 0; i < parsedSegments.size; i++)
            drawer.filledPolygonVector(parsedSegments.get(i), segmentVertices[i], getTinyVG());

        drawer.getBatch().flush();

        Gdx.gl.glColorMask(true, true, true, true);
        Gdx.gl.glStencilFunc(GL20.GL_EQUAL, 1, -1); // was GL_NOTEQUAL, 2, -1
        Gdx.gl.glStencilOp(GL20.GL_KEEP, GL20.GL_KEEP, GL20.GL_KEEP);

        for (int i = 0; i < parsedSegments.size; i++)
            drawer.filledPolygon(segmentVertices[i]);

        drawer.getBatch().flush();
        Gdx.gl.glDisable(GL20.GL_STENCIL_TEST);
        primaryStyle.end(drawer, viewport);
    }
}
