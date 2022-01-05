package dev.lyze.gdxtinyvg.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.LittleEndianInputStream;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxtinyvg.TinyVG;
import dev.lyze.gdxtinyvg.commands.headers.PathHeader;
import dev.lyze.gdxtinyvg.drawers.TinyVGShapeDrawer;
import dev.lyze.gdxtinyvg.enums.CommandType;
import dev.lyze.gdxtinyvg.enums.StyleType;
import dev.lyze.gdxtinyvg.types.ParsedPathSegment;
import java.io.IOException;

public class FillPathCommand extends Command {
    private PathHeader header;

    public FillPathCommand(TinyVG tinyVG) {
        super(CommandType.FILL_PATH, tinyVG);
    }

    @Override
    public void read(LittleEndianInputStream stream, StyleType primaryStyleType) throws IOException {
        header = new PathHeader(getTinyVG());
        header.read(stream, primaryStyleType, false);
    }

    @Override
    public void draw(TinyVGShapeDrawer drawer, Viewport viewport) {
        header.getPrimaryStyle().start(drawer, viewport);

        Gdx.gl.glEnable(GL20.GL_STENCIL_TEST);
        Gdx.gl.glColorMask(false, false, false, false);
        Gdx.gl.glStencilFunc(GL20.GL_ALWAYS, 0, -1);
        Gdx.gl.glStencilOp(GL20.GL_KEEP, GL20.GL_KEEP, GL20.GL_INCR);

        for (ParsedPathSegment parsedPathSegment : header.getSegments())
            drawer.filledPolygon(parsedPathSegment, getTinyVG());

        drawer.getBatch().flush();

        Gdx.gl.glColorMask(true, true, true, true);
        Gdx.gl.glStencilFunc(GL20.GL_EQUAL, 1, -1); // was GL_NOTEQUAL, 2, -1
        Gdx.gl.glStencilOp(GL20.GL_KEEP, GL20.GL_KEEP, GL20.GL_KEEP);

        for (ParsedPathSegment segment : header.getSegments())
            drawer.filledPolygon(segment.getVertices());

        drawer.getBatch().flush();
        Gdx.gl.glDisable(GL20.GL_STENCIL_TEST);
        header.getPrimaryStyle().end(drawer, viewport);
    }
}
